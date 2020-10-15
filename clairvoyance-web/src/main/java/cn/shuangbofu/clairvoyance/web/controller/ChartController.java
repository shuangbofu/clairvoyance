package cn.shuangbofu.clairvoyance.web.controller;

import cn.shuangbofu.clairvoyance.web.service.ChartService;
import cn.shuangbofu.clairvoyance.web.vo.ChartVO;
import cn.shuangbofu.clairvoyance.web.vo.Result;
import cn.shuangbofu.clairvoyance.web.vo.form.ChartForm;
import cn.shuangbofu.clairvoyance.web.vo.form.ChartLinkVO;
import cn.shuangbofu.clairvoyance.web.vo.form.ChartParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by shuangbofu on 2020/7/30 下午10:43
 */
@RestController
@RequestMapping("/api/chart")
@Api(tags = "图表接口")
public class ChartController {

    @Autowired
    private ChartService chartService;

    @DeleteMapping("/{chartId}")
    public Result<Boolean> deleteChart(@PathVariable("chartId") Long chartId) {
        return Result.success(chartService.deleteChart(chartId));
    }

    @GetMapping
    @ApiOperation("图表详情")
    public Result<ChartVO> getChart(@RequestParam("chartId") Long chartId) {
        return Result.success(chartService.getChart(chartId));
    }

    /**
     * @param chartFrom
     * @return
     */
    @PostMapping
    @ApiOperation("创建保存图表")
    public Result<Long> createChart(@RequestBody ChartVO chartFrom) {
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

    @PostMapping("/link")
    public Result<Boolean> createChartLink(@RequestBody ChartLinkVO chartLinkVO) {

        return Result.success(false);
    }

    @DeleteMapping("/link/{chartLinkId}")
    public Result<Boolean> removeChartLink(@PathVariable("chartLinkId") Long chartLinkId) {

        return Result.success(false);
    }
}
