package cn.shuangbofu.clairvoyance.web.controller;

import cn.shuangbofu.clairvoyance.web.pojo.Model.Chart.ChartModel;
import cn.shuangbofu.clairvoyance.web.pojo.VO.Chart.ChartVO;
import cn.shuangbofu.clairvoyance.web.service.ChartService;
import cn.shuangbofu.clairvoyance.web.vo.Result;
import cn.shuangbofu.clairvoyance.web.vo.form.ChartForm;
import cn.shuangbofu.clairvoyance.web.vo.form.ChartParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by shuangbofu on 2020/7/30 下午10:43
 */
@RestController
@RequestMapping("/api/chart")
public class ChartController {

    @Autowired
    private ChartService chartService;

    @DeleteMapping("/{chartId}")
    public Result<Boolean> deleteChart(@PathVariable("chartId") Long chartId) {
        return Result.success(chartService.deleteChart(chartId));
    }

    /**
     * 图表详情
     */
    @GetMapping
    public Result<ChartVO> getChart(@RequestParam("chartId") Long chartId) {
        return Result.success(chartService.getChart(chartId));
    }

    /**
     * 创建保存图表
     *
     * @param chartFrom
     * @return
     */
    @PostMapping
    public Result<Long> createChart(@RequestBody ChartModel chartFrom) {
        return Result.success(chartService.createChart(chartFrom));
    }

    @PostMapping("/data")
    public Result<List<Map<String, Object>>> getChartData(@RequestBody ChartForm form) {
        return Result.success(chartService.getChartData(form));
    }

    @PostMapping("/data/{chartId}")
    public Result<List<Map<String, Object>>> getChartData(@PathVariable("chartId") Long chartId, @RequestBody(required = false) ChartParam param) {
        return Result.success(chartService.getChartData(chartId, param));
    }
}
