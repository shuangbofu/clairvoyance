package cn.shuangbofu.clairvoyance.web.controller;

import cn.shuangbofu.clairvoyance.core.db.Dashboard;
import cn.shuangbofu.clairvoyance.core.db.DashboardFilter;
import cn.shuangbofu.clairvoyance.core.db.Node;
import cn.shuangbofu.clairvoyance.core.domain.Pair;
import cn.shuangbofu.clairvoyance.core.domain.chart.GlobalFilterParam;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.filter.ChartFilter;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.filter.ExactChartFilter;
import cn.shuangbofu.clairvoyance.core.enums.NodeType;
import cn.shuangbofu.clairvoyance.core.loader.ChartLoader;
import cn.shuangbofu.clairvoyance.core.loader.DashBoardLoader;
import cn.shuangbofu.clairvoyance.core.loader.DashboardFilterLoader;
import cn.shuangbofu.clairvoyance.core.loader.NodeLoader;
import cn.shuangbofu.clairvoyance.web.service.FieldService;
import cn.shuangbofu.clairvoyance.web.vo.*;
import cn.shuangbofu.clairvoyance.web.vo.form.DashboardForm;
import cn.shuangbofu.clairvoyance.web.vo.form.Folder;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/7/30 下午8:47
 */
@RestController
@RequestMapping("/api/dashboard")
@Api(tags = "仪表盘接口")
public class DashboardController {

    /**
     * 仪表盘目录（包含文件夹）
     *
     * @return
     */
    @GetMapping("/catalogue")
    @ApiOperation("仪表盘目录")
    public Result<List<Catalogue<DashboardSimpleVO>>> dashboards() {
        Pair<List<Node>, List<Long>> allNodesPair = NodeLoader.getAllNodesPair(NodeType.dashboard);
        List<DashboardSimpleVO> vos = DashBoardLoader.inIds(allNodesPair.getSecond()).stream().map(DashboardSimpleVO::toSimpleVO).collect(Collectors.toList());
        return Result.success(Catalogue.getList(allNodesPair.getFirst(), vos));
    }

    /**
     * 创建仪表盘
     *
     * @param form
     * @return
     */
    @PostMapping()
    @ApiOperation("创建仪表盘")
    public Result<Map<String, Long>> createDashboard(@RequestBody DashboardForm form) {

        // 创建仪表盘
        Dashboard dashboard = form.toModel();

        Long id = DashBoardLoader.create(dashboard);

        // 创建对应节点
        Long nodeId = NodeLoader.newNode(new Node().setName(form.getName())
                .setNodeType(NodeType.dashboard)
                .setRefId(id)
                .setParentId(form.getParentId()));

        Map<String, Long> object = new HashMap<>();
        object.put("nodeId", nodeId);
        object.put("dashboardId", id);
        return Result.success(object);
    }

    @GetMapping
    @ApiOperation(("根据ID获取仪表盘（布局配置，图表列表）"))
    public Result<DashboardVO> dashboard(@RequestParam("id") Long id) {
        List<ChartVO> chartVOS = ChartLoader.getChartsByDshId(id).stream().map(ChartVO::toVO).collect(Collectors.toList());

        Dashboard dashboard = DashBoardLoader.byId(id);

        return Result.success(DashboardVO.toVO(dashboard, chartVOS));
    }

    /**
     * 创建文件夹
     *
     * @param folder
     * @return
     */
    @PostMapping("/folder")
    @ApiOperation("创建文件夹")
    public Result<Folder> createFolder(@RequestBody Folder folder) {
        Long id = NodeLoader.newNode(folder.toNode().setNodeType(NodeType.dashboard));
        return Result.success(folder.setId(id));
    }

    /**
     * 保存仪表盘接口,目前主要保存样式位置
     *
     * @param vo
     * @return
     */
    @PutMapping
    public Result<Boolean> saveDashboard(@RequestBody DashboardVO vo) {
        DashBoardLoader.update(vo.toModel());
        return Result.success(true);
    }

    @PutMapping("/filter")
    public Result<Boolean> saveDashboardFilter(@RequestBody DashboardFilterVO dashboardFilterVO) {
        create(dashboardFilterVO.setParentId(0L));
        return Result.success(true);
    }

    private void create(DashboardFilterVO filterVO) {
        DashboardFilter dashboardFilter = filterVO.toModel();
        Long id = DashboardFilterLoader.create(dashboardFilter);
        List<DashboardFilterVO> children = filterVO.getChildren();
        if (children != null && children.size() > 0) {
            children.forEach(fVo -> create(fVo.setParentId(id)));
        }
    }

    @PostMapping("/filter/range/{dashboardFilterId}")
    public Result<RangeResult> filterRange(@PathVariable("dashboardFilterId") Long dashboardFilterId, @RequestBody List<GlobalFilterParam> params) {
        List<DashboardFilterVO> filterVOS = null;
        RangeResult rangeResult = new RangeResult();
        if (params != null && params.size() > 0) {
            List<Long> ids = params.stream().map(GlobalFilterParam::getDashboardFilterId).collect(Collectors.toList());
            List<DashboardFilter> filters = DashboardFilterLoader.inIds(ids);
            filterVOS = DashboardFilterVO.toVos(filters);
        }

        List<Object> range = Lists.newArrayList();
        DashboardFilter dashboardFilter = DashboardFilterLoader.byId(dashboardFilterId);
        DashboardFilterVO vo = DashboardFilterVO.toVO(dashboardFilter);
        List<String> template = vo.getTemplate();
        if (template != null && template.size() > 0) {
            range.addAll(template);
            return Result.success(new RangeResult(range));
        }
        for (Long workSheetId : vo.getSheetFieldMap().keySet()) {
            List<ChartFilter> chartFilters = Lists.newArrayList();
            if (filterVOS != null && filterVOS.size() > 0) {
                filterVOS.forEach(filterVO -> {
                    Long fieldId = filterVO.getSheetFieldMap().get(workSheetId);
                    if (fieldId != null) {
                        Optional<GlobalFilterParam> any = params.stream().filter(i -> i.getDashboardFilterId().equals(filterVO.getId())).findAny();
                        any.ifPresent(globalFilterParam -> chartFilters.add(new ExactChartFilter(globalFilterParam.getRange(), filterVO.getIncluded(), fieldId)));
                    }
                });
            }
            RangeResult result = FieldService.getFieldRange(workSheetId, vo.getSheetFieldMap().get(workSheetId), chartFilters);
            rangeResult.concat(result);
        }
        return Result.success(rangeResult);
    }
}
