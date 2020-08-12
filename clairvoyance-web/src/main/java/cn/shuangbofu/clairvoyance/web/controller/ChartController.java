package cn.shuangbofu.clairvoyance.web.controller;

import cn.shuangbofu.clairvoyance.core.db.Chart;
import cn.shuangbofu.clairvoyance.core.db.WorkSheet;
import cn.shuangbofu.clairvoyance.core.domain.chart.ChartSql;
import cn.shuangbofu.clairvoyance.core.domain.chart.DrillParam;
import cn.shuangbofu.clairvoyance.core.domain.chart.SqlBuiler;
import cn.shuangbofu.clairvoyance.core.loader.ChartLoader;
import cn.shuangbofu.clairvoyance.core.loader.WorkSheetLoader;
import cn.shuangbofu.clairvoyance.core.meta.source.SourceTable;
import cn.shuangbofu.clairvoyance.core.query.SqlQueryRunner;
import cn.shuangbofu.clairvoyance.web.vo.ChartVO;
import cn.shuangbofu.clairvoyance.web.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by shuangbofu on 2020/7/30 下午10:43
 */
@RestController
@RequestMapping("/chart")
@Api(tags = "图表接口")
public class ChartController {

    @GetMapping
    @ApiOperation("图表详情")
    public Result<ChartVO> getChart(@RequestParam("chartId") Long chartId) {
        return Result.success(ChartVO.toVO(ChartLoader.byId(chartId)));
    }

    /**
     * @param chartFrom
     * @return
     */
    @PostMapping
    @ApiOperation("创建保存图表")
    public Result<Long> createChart(@RequestBody ChartVO chartFrom) {
        Chart chart = chartFrom.toModel();
        if (chartFrom.created()) {
            ChartLoader.update(chart);
        } else {
            Long id = ChartLoader.create(chart);
            chart.setId(id);
        }
        return Result.success(chart.getId());
    }

    @GetMapping("/data")
    @ApiOperation("查询图表数据")
    public Result<List<Map<String, Object>>> getChartData(@RequestParam("chartId") Long chartId, @RequestBody(required = false) DrillParam param) {
        Chart chart = ChartLoader.byId(chartId);
        Long workSheetId = chart.getWorkSheetId();
        WorkSheet workSheet = WorkSheetLoader.getSheet(workSheetId);
        SourceTable table = SqlQueryRunner.getSourceTable(workSheet);
        ChartSql chartSql = SqlBuiler.buildChartSql(chart.getSqlConfig(), workSheetId)
                .setDrill(param);
        List<Map<String, Object>> result = table.run(chartSql);
        return Result.success(result);
    }
}
