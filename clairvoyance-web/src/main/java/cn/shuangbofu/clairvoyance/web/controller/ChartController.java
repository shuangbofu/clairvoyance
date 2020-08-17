package cn.shuangbofu.clairvoyance.web.controller;

import cn.shuangbofu.clairvoyance.core.db.Chart;
import cn.shuangbofu.clairvoyance.core.db.Dashboard;
import cn.shuangbofu.clairvoyance.core.db.DashboardFilter;
import cn.shuangbofu.clairvoyance.core.db.WorkSheet;
import cn.shuangbofu.clairvoyance.core.domain.chart.ChartSqlBuilder;
import cn.shuangbofu.clairvoyance.core.domain.chart.GlobalFilterParam;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.filter.ExactChartFilter;
import cn.shuangbofu.clairvoyance.core.loader.ChartLoader;
import cn.shuangbofu.clairvoyance.core.loader.DashBoardLoader;
import cn.shuangbofu.clairvoyance.core.loader.DashboardFilterLoader;
import cn.shuangbofu.clairvoyance.core.loader.WorkSheetLoader;
import cn.shuangbofu.clairvoyance.core.meta.source.SourceTable;
import cn.shuangbofu.clairvoyance.core.query.SqlQueryRunner;
import cn.shuangbofu.clairvoyance.core.utils.JSON;
import cn.shuangbofu.clairvoyance.web.vo.ChartVO;
import cn.shuangbofu.clairvoyance.web.vo.DashboardFilterVO;
import cn.shuangbofu.clairvoyance.web.vo.LayoutConfig;
import cn.shuangbofu.clairvoyance.web.vo.Result;
import cn.shuangbofu.clairvoyance.web.vo.form.ChartParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Result<List<Map<String, Object>>> getChartData(@PathVariable("chartId") Long chartId, @RequestBody(required = false) ChartParam param) {
        Chart chart = ChartLoader.byId(chartId);
        Long workSheetId = chart.getWorkSheetId();

        WorkSheet workSheet = WorkSheetLoader.getSheet(workSheetId);
        SourceTable table = SqlQueryRunner.getSourceTable(workSheet);

        ChartSqlBuilder sqlBuilder = new ChartSqlBuilder(chart.getSqlConfig(), workSheetId)
                // 设置下钻属性
                .setDrillParam(param.getDrillParam());
        List<GlobalFilterParam> globalFilterParams = param.getGlobalFilterParams();
        if (globalFilterParams != null && globalFilterParams.size() > 0) {
            List<Long> dashboardFilterIds = globalFilterParams.stream()
                    .map(GlobalFilterParam::getDashboardFilterId).collect(Collectors.toList());
            List<DashboardFilter> filters = DashboardFilterLoader.inIds(dashboardFilterIds);
            DashboardFilterVO.toVos(filters).stream()
                    .filter(i -> i.getSelectedCharts()
                            .contains(chartId))
                    .forEach(filterVO -> {
                        Long fieldId = filterVO.getSheetFieldMap().get(workSheetId);
                        Optional<GlobalFilterParam> any = globalFilterParams.stream().filter(p -> p.getDashboardFilterId().equals(filterVO.getId())).findAny();
                        if (any.isPresent()) {
                            ExactChartFilter exactChartFilter = new ExactChartFilter(any.get().getRange(), filterVO.getIncluded(), fieldId);
                            // 添加全局过滤器
                            sqlBuilder.addFilter(exactChartFilter);
                        }
                    });
        }
        // 执行SQL获得结果
        List<Map<String, Object>> result = table.run(sqlBuilder.build());
        return Result.success(result);
    }
}
