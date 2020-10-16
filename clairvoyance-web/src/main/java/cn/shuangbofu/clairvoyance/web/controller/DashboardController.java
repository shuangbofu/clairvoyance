package cn.shuangbofu.clairvoyance.web.controller;

import cn.shuangbofu.clairvoyance.core.chart.GlobalFilterParam;
import cn.shuangbofu.clairvoyance.web.service.DashboardService;
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

    /**
     * 根据ID获取仪表盘（布局配置，图表列表）
     *
     * @param dashboardId
     * @return
     */
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
        return Result.success(dashboardService.createFolder(folder));
    }

    @PutMapping("/move")
    public Result<Boolean> moveNode(@RequestBody Folder folder) {
        return Result.success(dashboardService.moveNode(folder));
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

    @PutMapping("/filter")
    public Result<Boolean> saveDashboardFilter(@RequestBody DashboardFilterVO dashboardFilterVO) {
        return Result.success(dashboardService.saveDashboardFilter(dashboardFilterVO));
    }

    @PostMapping("/filter/range/{dashboardFilterId}")
    public Result<List<Object>> filterRange(@PathVariable("dashboardFilterId") Long dashboardFilterId, @RequestBody(required = false) List<GlobalFilterParam> params) {
        return Result.success(dashboardService.getFilterRange(dashboardFilterId, params));
    }

    @GetMapping("/workSheet")
    public Result<List<WorkSheetVO>> workSheetByDashboard(@RequestParam("dashboardId") Long dashboardId) {
        return Result.success(workSheetService.getWorkSheetsByDashboardId(dashboardId));
    }
}
