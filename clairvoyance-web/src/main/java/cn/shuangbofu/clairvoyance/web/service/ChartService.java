package cn.shuangbofu.clairvoyance.web.service;

import cn.shuangbofu.clairvoyance.core.chart.ChartSqlBuilder;
import cn.shuangbofu.clairvoyance.core.chart.filter.ExactChartFilter;
import cn.shuangbofu.clairvoyance.core.meta.source.SourceTable;
import cn.shuangbofu.clairvoyance.core.utils.JSON;
import cn.shuangbofu.clairvoyance.web.config.CurrentLoginUser;
import cn.shuangbofu.clairvoyance.web.dao.*;
import cn.shuangbofu.clairvoyance.web.entity.Chart;
import cn.shuangbofu.clairvoyance.web.entity.Dashboard;
import cn.shuangbofu.clairvoyance.web.entity.DashboardLink;
import cn.shuangbofu.clairvoyance.web.entity.WorkSheet;
import cn.shuangbofu.clairvoyance.web.pojo.Model.Chart.ChartModel;
import cn.shuangbofu.clairvoyance.web.pojo.Model.Chart.ChartQueryDataSearchParamsModel;
import cn.shuangbofu.clairvoyance.web.pojo.VO.Chart.ChartVO;
import cn.shuangbofu.clairvoyance.web.vo.FieldSimpleVO;
import cn.shuangbofu.clairvoyance.web.vo.LayoutConfig;
import cn.shuangbofu.clairvoyance.web.vo.form.ChartForm;
import cn.shuangbofu.clairvoyance.web.vo.form.ChartParam;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by shuangbofu on 2020/10/14 19:46
 */
@Component
public class ChartService {

    private final DashBoardDao dashBoardDao = Daos.dashboard();
    private final DashboardLinkDao dashboardLinkDao = Daos.dashboardLinkDao();
    private final ChartDao chartDao = Daos.chart();
    private final WorkSheetDao workSheetDao = Daos.workSheet();
    private final SheetFieldDao sheetFieldDao = Daos.sheetFieldDao();

    @Autowired
    private SqlQueryRunner sqlQueryRunner;
    @Autowired
    private Converter converter;
    @Autowired
    private cn.shuangbofu.clairvoyance.web.service.FieldService fieldService;

    public Boolean deleteChart(Long chartId) {
        Chart chart = chartDao.findOneById(chartId);
        if (chart != null) {
            Daos.atomic(() -> {
                Long dashboardId = chart.getDashboardId();
                Dashboard dashboard = dashBoardDao.findOneById(dashboardId);
                String layoutConfig = dashboard.getLayoutConfig();
                LayoutConfig config = JSON.parseObject(layoutConfig, LayoutConfig.class);
                boolean b = config.getPositions().removeIf(i -> i.getId().equals(chartId));
                if (b) {
                    dashBoardDao.updateLayout(dashboardId, JSON.toJSONString(config));
                }
            }, "deleteChart error");
            return true;
        }
        return false;
    }

    public ChartVO getChart(Long chartId) {
        ChartVO chartVO = converter.chart2VO(chartDao.findOneById(chartId));
        chartVO.setComputeFields(FieldSimpleVO.toVOs(sheetFieldDao.getComputeFieldList(chartId)));
        return chartVO;
    }

    public Long createChart(ChartModel chartFrom) {
        if (chartFrom.getChartId() != null && chartFrom.getSqlConfig() != null
                && chartFrom.getSqlConfig().getLayers() != null
                && chartFrom.getSqlConfig().getLayers().size() > 1) {
            List<DashboardLink> dashboardLinkList = dashboardLinkDao.getListByChartId(chartFrom.getChartId());
            if (dashboardLinkList != null && dashboardLinkList.size() > 0) {
                throw new RuntimeException("联动主表无法设置多图层");
            }
        }
        Chart chart = chartFrom.toModel();
        if (chartFrom.created()) {
            chart.setModifyUser(CurrentLoginUser.getUser());
            chartDao.updateModel(chart);
        } else {
            chart.setCreateUser(CurrentLoginUser.getUser());
            chart.setModifyUser(CurrentLoginUser.getUser());
            Long id = chartDao.insert(chart);
            chart.setId(id);
            // FIXME: 2020/8/14
            // 更新dashboard
            Long dashboardId = chart.getDashboardId();
            Dashboard dashboard = dashBoardDao.findOneById(dashboardId);
            LayoutConfig layoutConfig = JSON.parseObject(dashboard.getLayoutConfig(), LayoutConfig.class);
            layoutConfig.getPositions().add(new LayoutConfig.Layout(layoutConfig.getMaxBottom(), id));
            dashBoardDao.updateLayout(dashboardId, JSON.toJSONString(layoutConfig));
            // TODO 如果sqlConfig中包含钻取的配置，那么需要去掉当前图表的工作表作为主表的多表关联
        }
        //关联计算字段
        if (chart.getId() != null && chartFrom.getLinkComputeFields() != null) {
            sheetFieldDao.linkChart4ComputeField(chart.getId(), chartFrom.getLinkComputeFields());
        }
        return chart.getId();
    }

    public List<Map<String, Object>> getChartData(ChartForm form) {
        List<Map<String, Object>> result = new ArrayList<>();
        if (form != null && form.getSqlConfig() != null && !form.getSqlConfig().isEmptyChartSQL()) {
            String sqlConfig = form.getSqlConfig().toJSONString();
            Long workSheetId = form.getWorkSheetId();
            ChartSqlBuilder sqlBuilder = new ChartSqlBuilder(sqlConfig, fieldService.getFields(workSheetId));
            sqlBuilder.setDrillParam(form.getDrillParam());
            List<ExactChartFilter> searchParams = new ArrayList<>();
            if (form.getSearchParams() != null) {
                for (ChartQueryDataSearchParamsModel searchParamsModel : form.getSearchParams()) {
                    ExactChartFilter searchParam = new ExactChartFilter();
                    searchParam.setId(searchParamsModel.getId());
                    searchParam.setIncluded(searchParamsModel.getIncluded());
                    searchParam.setRange(searchParamsModel.getRange());
                    searchParams.add(searchParam);
                }
            }
            sqlBuilder.addFilters(Lists.newArrayList(searchParams));
            // 执行SQL获得结果
            result = runChartSql(workSheetId, form.getDashboardId(), sqlBuilder);
        }
        return result;
    }

    private List<Map<String, Object>> runChartSql(Long workSheetId, Long dashboardId, ChartSqlBuilder sqlBuilder) {
        WorkSheet workSheet = workSheetDao.findOneById(workSheetId);
        SourceTable sourceTable = sqlQueryRunner.getSourceTable(workSheet);
        return sourceTable.run(sqlBuilder.build());
    }

    public List<Map<String, Object>> getChartData(Long chartId, ChartParam param) {
        Chart chart = chartDao.findOneById(chartId);
        ChartSqlBuilder sqlBuilder = new ChartSqlBuilder(chart.getSqlConfig(), fieldService.getFields(chart.getWorkSheetId()));
        sqlBuilder.setDrillParam(param.getDrillParam());
        List<ExactChartFilter> searchParams = new ArrayList<>();
        if (param.getSearchParams() != null) {
            for (ChartQueryDataSearchParamsModel searchParamsModel : param.getSearchParams()) {
                ExactChartFilter searchParam = new ExactChartFilter();
                searchParam.setIncluded(searchParamsModel.getIncluded());
                searchParam.setId(searchParamsModel.getId());
                searchParam.setRange(searchParamsModel.getRange());
                searchParams.add(searchParam);
            }
        }
        sqlBuilder.addFilters(Lists.newArrayList(searchParams));
        // 执行SQL获得结果
        return runChartSql(chart.getWorkSheetId(), chart.getDashboardId(), sqlBuilder);
    }
}
