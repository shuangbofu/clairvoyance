package cn.shuangbofu.clairvoyance.web.controller;

import cn.shuangbofu.clairvoyance.core.chart.GlobalFilterParam;
import cn.shuangbofu.clairvoyance.web.pojo.Model.Dashboard.ChartLinkModel;
import cn.shuangbofu.clairvoyance.web.pojo.VO.Dashboard.DashboardFilterSaveVO;
import cn.shuangbofu.clairvoyance.web.service.DashboardService;
import cn.shuangbofu.clairvoyance.web.service.NodeService;
import cn.shuangbofu.clairvoyance.web.service.WorkSheetService;
import cn.shuangbofu.clairvoyance.web.vo.*;
import cn.shuangbofu.clairvoyance.web.vo.form.DashboardForm;
import cn.shuangbofu.clairvoyance.web.vo.form.Folder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by shuangbofu on 2020/7/30 下午8:47
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private WorkSheetService workSheetService;

    @Autowired
    private NodeService nodeService;

    /**
     * 仪表盘目录（包含文件夹）
     *
     * @return
     */
    @GetMapping("/catalogue")
    public Result<List<Catalogue<DashboardSimpleVO>>> dashboards() {
        return Result.success(dashboardService.getCatalogues());
    }

    /**
     * 创建仪表盘
     *
     * @param form
     * @return
     */
    @PostMapping()
    public Result<Map<String, Long>> createDashboard(@RequestBody DashboardForm form) {
        return Result.success(dashboardService.createDashboard(form));
    }

    @GetMapping
    public Result<DashboardVO> dashboard(@RequestParam("dashboardId") Long dashboardId) {
        return Result.success(dashboardService.getDashboard(dashboardId));
    }

    /**
     * 创建文件夹
     *
     * @param folder
     * @return
     */
    @PostMapping("/folder")
    public Result<Folder> createFolder(@RequestBody Folder folder) {
        return Result.success(nodeService.createFolder(folder));
    }

    @PutMapping("/folder")
    public Result<Boolean> modifyNode(@RequestBody Folder folder) {
        return Result.success(nodeService.update4Folder(folder));
    }

    @PostMapping("/move")
    public Result<Boolean> removeNode(@RequestBody Folder folder) {
        return Result.success(nodeService.moveNode(folder.getId(), folder.getParentId()));
    }

    @DeleteMapping("/{dashboardId}")
    public Result<Boolean> removeDashboard(@PathVariable("dashboardId") Long dashboardId) {
        return Result.success(dashboardService.removeDashboard(dashboardId));
    }

    @DeleteMapping("/folder/{id}")
    public Result<Boolean> removeFolder(@PathVariable("id") Long id) {
        return Result.success(dashboardService.removeFolder(id));
    }

    /**
     * 保存仪表盘接口,目前主要保存样式位置
     *
     * @param vo
     * @return
     */
    @PutMapping
    public Result<Boolean> saveDashboard(@RequestBody DashboardVO vo) {
        return Result.success(dashboardService.saveDashboard(vo));
    }

    @PostMapping("/filter")
    public Result<List<DashboardFilterVO>> saveDashboardFilter(@RequestBody DashboardFilterSaveVO dashboardFilterSaveVO) {
        return Result.success(dashboardService.saveBatchDashboardFilter(dashboardFilterSaveVO));
    }

    @PostMapping("/filter/range/{dashboardFilterId}")
    public Result<List<Object>> filterRange(@PathVariable("dashboardFilterId") Long dashboardFilterId,
                                            @RequestBody(required = false) List<GlobalFilterParam> params) {
        return Result.success(dashboardService.getFilterRange(dashboardFilterId, params));
    }

    @GetMapping("/workSheet")
    public Result<List<WorkSheetVO>> workSheetByDashboard(@RequestParam("dashboardId") Long dashboardId) {
        return Result.success(workSheetService.getWorkSheetsByDashboardId(dashboardId));
    }

    /**
     * 保存图表关联信息
     *
     * @param chartLinkModel
     * @return
     */
    @PostMapping("/set/link")
    public Result<Boolean> setChartLink(@RequestBody ChartLinkModel chartLinkModel) {
        return Result.success(dashboardService.setChartLink(chartLinkModel));
    }

    /**
     * 根据图表ID删除图表关联
     *
     * @param chartId
     * @return
     */
    @DeleteMapping("/link/{chartId}")
    public Result<Boolean> removeChartLink(@PathVariable("chartId") Long chartId) {
        return Result.success(dashboardService.removeChartLink(chartId));
    }
}
