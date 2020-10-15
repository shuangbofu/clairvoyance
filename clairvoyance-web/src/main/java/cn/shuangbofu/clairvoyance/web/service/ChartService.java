package cn.shuangbofu.clairvoyance.web.service;

import cn.shuangbofu.clairvoyance.core.chart.ChartSqlBuilder;
import cn.shuangbofu.clairvoyance.core.chart.GlobalFilterParam;
import cn.shuangbofu.clairvoyance.core.chart.LinkedParam;
import cn.shuangbofu.clairvoyance.core.chart.sql.filter.ExactChartFilter;
import cn.shuangbofu.clairvoyance.core.meta.source.SourceTable;
import cn.shuangbofu.clairvoyance.core.utils.JSON;
import cn.shuangbofu.clairvoyance.web.dao.ChartDao;
import cn.shuangbofu.clairvoyance.web.dao.DashBoardDao;
import cn.shuangbofu.clairvoyance.web.dao.DashboardFilterDao;
import cn.shuangbofu.clairvoyance.web.dao.WorkSheetDao;
import cn.shuangbofu.clairvoyance.web.entity.Chart;
import cn.shuangbofu.clairvoyance.web.entity.Dashboard;
import cn.shuangbofu.clairvoyance.web.entity.DashboardFilter;
import cn.shuangbofu.clairvoyance.web.entity.WorkSheet;
import cn.shuangbofu.clairvoyance.web.vo.ChartVO;
import cn.shuangbofu.clairvoyance.web.vo.DashboardFilterVO;
import cn.shuangbofu.clairvoyance.web.vo.LayoutConfig;
import cn.shuangbofu.clairvoyance.web.vo.form.ChartForm;
import cn.shuangbofu.clairvoyance.web.vo.form.ChartParam;
import io.github.biezhi.anima.Anima;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/10/14 19:46
 */
@Component
public class ChartService {

    public Boolean deleteChart(Long chartId) {
        Chart chart = ChartDao.byId(chartId);
        if (chart != null) {
            Anima.atomic(() -> {
                Long dashboardId = chart.getDashboardId();
                Dashboard dashboard = DashBoardDao.byId(dashboardId);
                String layoutConfig = dashboard.getLayoutConfig();
                LayoutConfig config = JSON.parseObject(layoutConfig, LayoutConfig.class);
                boolean b = config.getPositions().removeIf(i -> i.getId().equals(chartId));
                if (b) {
                    dashboard.setLayoutConfig(JSON.toJSONString(config))
                            .update();
                }
                chart.delete();
            }).catchException(e -> {
                throw new RuntimeException(e);
            });
            return true;
        }
        return false;
    }

    public ChartVO getChart(Long chartId) {
        return ChartVO.toVO(ChartDao.byId(chartId));
    }

    public Long createChart(ChartVO chartFrom) {
        Chart chart = chartFrom.toModel();
        if (chartFrom.created()) {
//            chart.setModifyUser(CurrentLoginUser.getUser());
            ChartDao.update(chart);
        } else {
//            chart.setCreateUser(CurrentLoginUser.getUser());
//            chart.setModifyUser(CurrentLoginUser.getUser());
            Long id = ChartDao.create(chart);
            chart.setId(id);
            // FIXME: 2020/8/14
            // 更新dashboard
            Long dashboardId = chart.getDashboardId();
            Dashboard dashboard = DashBoardDao.byId(dashboardId);
            LayoutConfig layoutConfig = JSON.parseObject(dashboard.getLayoutConfig(), LayoutConfig.class);
            layoutConfig.getPositions().add(new LayoutConfig.Layout(layoutConfig.getMaxBottom(), id));
            Dashboard newDash = new Dashboard().setId(dashboardId).setLayoutConfig(JSON.toJSONString(layoutConfig));
            DashBoardDao.update(newDash);
            // TODO 如果sqlConfig中包含钻取的配置，那么需要去掉当前图表的工作表作为主表的多表关联
        }
        return chart.getId();
    }


    public List<Map<String, Object>> getChartData(ChartForm form) {
        String sqlConfig = form.getSqlConfig().toJSONString();
        Long workSheetId = form.getWorkSheetId();
        ChartSqlBuilder sqlBuilder = ChartSqlBuilderFactory.create(sqlConfig, workSheetId);
        sqlBuilder.setDrillParam(form.getDrillParam());
        setGlobalFilters(sqlBuilder, form.getGlobalFilterParams(), form.getChartId(), workSheetId);
        setLinkedParam(sqlBuilder, form.getLinkedParam());

        return runChartSql(workSheetId, sqlBuilder);
    }

    public List<Map<String, Object>> getChartData(Long chartId, ChartParam param) {
        Chart chart = ChartDao.byId(chartId);
        Long workSheetId = chart.getWorkSheetId();

        ChartSqlBuilder sqlBuilder = ChartSqlBuilderFactory.create(chart.getSqlConfig(), workSheetId)
                // 设置下钻属性
                .setDrillParam(param.getDrillParam());

        setGlobalFilters(sqlBuilder, param.getGlobalFilterParams(), chartId, workSheetId);
        setLinkedParam(sqlBuilder, param.getLinkedParam());

        // 执行SQL获得结果
        return runChartSql(workSheetId, sqlBuilder);
    }

    private SourceTable getQuerySourceTable(Long workSheetId) {
        WorkSheet workSheet = WorkSheetDao.getSheet(workSheetId);
        return SqlQueryRunner.getSourceTable(workSheet);
    }

    private List<Map<String, Object>> runChartSql(Long workSheetId, ChartSqlBuilder sqlBuilder) {
        return getQuerySourceTable(workSheetId).run(sqlBuilder.build());
    }


    /**
     * TODO 设置图表联动
     */
    private void setLinkedParam(ChartSqlBuilder sqlBuilder, LinkedParam param) {

    }

    /**
     * 设置全局筛选
     *
     * @param sqlBuilder
     * @param globalFilterParams
     * @param chartId
     * @param workSheetId
     */
    private void setGlobalFilters(ChartSqlBuilder sqlBuilder, List<GlobalFilterParam> globalFilterParams, Long chartId, Long workSheetId) {
        if (globalFilterParams != null && globalFilterParams.size() > 0) {
            List<Long> dashboardFilterIds = globalFilterParams.stream()
                    .map(GlobalFilterParam::getDashboardFilterId).collect(Collectors.toList());
            List<DashboardFilter> filters = DashboardFilterDao.inIds(dashboardFilterIds);
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
    }
}
