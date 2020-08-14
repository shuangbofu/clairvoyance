package cn.shuangbofu.clairvoyance.web.controller;

import cn.shuangbofu.clairvoyance.core.db.Chart;
import cn.shuangbofu.clairvoyance.core.db.Dashboard;
import cn.shuangbofu.clairvoyance.core.db.WorkSheet;
import cn.shuangbofu.clairvoyance.core.domain.chart.ChartSql;
import cn.shuangbofu.clairvoyance.core.domain.chart.DrillParam;
import cn.shuangbofu.clairvoyance.core.domain.chart.SqlBuiler;
import cn.shuangbofu.clairvoyance.core.loader.ChartLoader;
import cn.shuangbofu.clairvoyance.core.loader.DashBoardLoader;
import cn.shuangbofu.clairvoyance.core.loader.WorkSheetLoader;
import cn.shuangbofu.clairvoyance.core.meta.source.SourceTable;
import cn.shuangbofu.clairvoyance.core.query.SqlQueryRunner;
import cn.shuangbofu.clairvoyance.core.utils.JSON;
import cn.shuangbofu.clairvoyance.web.vo.ChartVO;
import cn.shuangbofu.clairvoyance.web.vo.LayoutConfig;
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
@RequestMapping("/api/chart")
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

            // FIXME: 2020/8/14
            // 更新dashboard
            Long dashboardId = chart.getDashboardId();
            Dashboard dashboard = DashBoardLoader.byId(dashboardId);
            LayoutConfig layoutConfig = JSON.parseObject(dashboard.getLayoutConfig(), LayoutConfig.class);
            layoutConfig.getPositions().add(new LayoutConfig.Layout(layoutConfig.getMaxBottom(), id));
            Dashboard newDash = new Dashboard().setId(dashboardId).setLayoutConfig(JSON.toJSONString(layoutConfig));
            DashBoardLoader.update(newDash);
        }
        return Result.success(chart.getId());
    }

    @PostMapping("/data/{chartId}")
    public Result<List<Map<String, Object>>> getChartData(@PathVariable("chartId") Long chartId, @RequestBody(required = false) DrillParam param) {
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
