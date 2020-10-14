package cn.shuangbofu.clairvoyance.web.controller;

import cn.shuangbofu.clairvoyance.core.db.Dashboard;
import cn.shuangbofu.clairvoyance.core.db.DashboardFilter;
import cn.shuangbofu.clairvoyance.core.db.Node;
import cn.shuangbofu.clairvoyance.core.domain.Pair;
import cn.shuangbofu.clairvoyance.core.domain.chart.GlobalFilterParam;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.filter.ChartFilter;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.filter.ExactChartFilter;
import cn.shuangbofu.clairvoyance.core.enums.NodeType;
import cn.shuangbofu.clairvoyance.core.loader.*;
import cn.shuangbofu.clairvoyance.web.service.FieldService;
import cn.shuangbofu.clairvoyance.web.vo.*;
import cn.shuangbofu.clairvoyance.web.vo.form.DashboardForm;
import cn.shuangbofu.clairvoyance.web.vo.form.Folder;
import com.google.common.collect.Lists;
import io.github.biezhi.anima.Anima;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.shuangbofu.clairvoyance.core.utils.Functions.ifNotNullThen;
import static cn.shuangbofu.clairvoyance.core.utils.Functions.ifThen;

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
    public Result<DashboardVO> dashboard(@RequestParam("dashboardId") Long dashboardId) {
        List<ChartVO> chartVOS = ChartLoader.getChartsByDshId(dashboardId).stream().map(ChartVO::toVO).collect(Collectors.toList());

        Dashboard dashboard = DashBoardLoader.byId(dashboardId);

        return Result.success(DashboardVO.toVO(dashboard, chartVOS));
    }

    @DeleteMapping("/{dashboardId}")
    public Result<Boolean> removeDashboard(@PathVariable("dashboardId") Long dashboardId) {
        Anima.atomic(() -> {
            DashBoardLoader.delete(dashboardId);
            NodeLoader.removeByRefId(dashboardId, NodeType.dashboard);
        }).catchException(e -> {
            throw new RuntimeException(e);
        });
        return Result.success(true);
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

    @PutMapping("/folder")
    public Result<Boolean> modifyFolder(@RequestBody Folder folder) {
        NodeLoader.update(folder.toNode());
        return Result.success(true);
    }


    /**
     * 修改名称、remarks、parentId等等
     *
     * @param form
     * @return
     */
    @PutMapping
    public Result<Boolean> modifyDashboard(@RequestBody DashboardForm form) {
        Long dashboardId = form.getDashboardId();
        Dashboard origin = DashBoardLoader.byId(dashboardId);
        ifNotNullThen(origin, () -> {
            Node node = NodeLoader.getNodeByRefId(dashboardId, NodeType.dashboard);
            ifNotNullThen(node, () -> {
                Long newParentId = form.getParentId();
                String newName = form.getName();
                ifThen(!node.getParentId().equals(newParentId), () -> node.setParentId(newParentId));
                ifThen(!node.getName().equals(newName), () -> node.setName(newName));
                NodeLoader.update(node);
            });
        });
        return Result.success(true);
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
    public Result<List<Object>> filterRange(@PathVariable("dashboardFilterId") Long dashboardFilterId, @RequestBody(required = false) List<GlobalFilterParam> params) {
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
            return Result.success(range);
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
        return Result.success(rangeResult.getRange());
    }

    @GetMapping("/workSheet")
    public Result<List<WorkSheetVO>> workSheetByDashboard(@RequestParam("dashboardId") Long dashboardId) {
        List<Long> workSheetIds = ChartLoader.getWorkSheetIdsByDashboardId(dashboardId);
        return Result.success(WorkSheetVO.toVOList(WorkSheetLoader.simpleInIds(workSheetIds)));
    }
}
