package cn.shuangbofu.clairvoyance.web.service;

import cn.shuangbofu.clairvoyance.core.chart.AlarmConfig;
import cn.shuangbofu.clairvoyance.core.chart.ChartLayer;
import cn.shuangbofu.clairvoyance.core.chart.ChartSql;
import cn.shuangbofu.clairvoyance.core.chart.ChartSqlBuilder;
import cn.shuangbofu.clairvoyance.core.chart.field.Value;
import cn.shuangbofu.clairvoyance.core.utils.JSON;
import cn.shuangbofu.clairvoyance.web.dao.Daos;
import cn.shuangbofu.clairvoyance.web.dao.DashboardFilterDao;
import cn.shuangbofu.clairvoyance.web.dao.DashboardFilterSelectedDao;
import cn.shuangbofu.clairvoyance.web.dao.DashboardLinkDao;
import cn.shuangbofu.clairvoyance.web.entity.*;
import cn.shuangbofu.clairvoyance.web.enums.ChartType;
import cn.shuangbofu.clairvoyance.web.pojo.VO.Chart.ChartVO;
import cn.shuangbofu.clairvoyance.web.pojo.VO.Dashboard.ChartLinkVO;
import cn.shuangbofu.clairvoyance.web.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/10/17 下午11:44
 */
@Component
public class Converter {

    private final DashboardFilterDao dashboardFilterDao = Daos.dashboardFilter();
    private final DashboardFilterSelectedDao dashboardFilterSelectedDao = Daos.dashboardFilterSelectedDao();
    private final DashboardLinkDao dashboardLinkDao = Daos.dashboardLinkDao();
    @Autowired
    private cn.shuangbofu.clairvoyance.web.service.FieldService fieldService;

    public WorkSheetVO worksheet2VO(WorkSheet workSheet) {
        WorkSheetVO vo = new WorkSheetVO()
                .setSheetType(workSheet.getSheetType())
                .setFields(fieldService.getAllField4Origin(workSheet.getId()));

        vo.setWorkSheetId(workSheet.getId())
                .setDescription(workSheet.getDescription())
                .setTitle(workSheet.getTitle())
                .setTableName(workSheet.getTableName());
        return vo;
    }

    public List<WorkSheetVO> worksheets2VOList(List<WorkSheet> workSheets) {
        return workSheets.stream().map(this::worksheet2VO).collect(Collectors.toList());
    }

    public ChartVO chart2VO(Chart chart) {
        ChartSql sqlConfig = new ChartSqlBuilder(chart.getSqlConfig(), fieldService.getFields(chart.getWorkSheetId())).build();
        List<ChartLayoutConfig> configs = JSON.parseArray(chart.getLayoutConfig(), ChartLayoutConfig.class);
        // 只有表格才有行总计
        for (int i = 0; i < sqlConfig.getLayers().size(); i++) {
            ChartLayer layer = sqlConfig.getLayers().get(i);
            ChartLayoutConfig layoutConfig = configs.get(i);
            if (!ChartType.C1.equals(layoutConfig.getChartType())) {
                layer.getYWithoutRow().removeIf(Value::total);
            }
        }

        ChartVO chartVO = new ChartVO();
        chartVO.setName(chart.getName());
        chartVO.setRemarks(chart.getRemarks());
        chartVO.setChartId(chart.getId());
        chartVO.setDashboardId(chart.getDashboardId());
        chartVO.setWorkSheetId(chart.getWorkSheetId());
        chartVO.setLayoutConfigs(configs);
        chartVO.setAlarmConfig(JSON.parseObject(chart.getAlarmConfig(), AlarmConfig.class));
        chartVO.setWorkSheetId(chart.getWorkSheetId());
        chartVO.setSqlConfig(sqlConfig);
        chartVO.setCreateUser(UserCache.getUser(chart.getCreateUser()));
        chartVO.setModifyUser(UserCache.getUser(chart.getModifyUser()));

        List<DashboardLink> dashboardLinkList = dashboardLinkDao.getListByChartId(chart.getId());
        if (dashboardLinkList != null && dashboardLinkList.size() > 0) {
            chartVO.setHasChartLink(true);
        }

        // fields更新到sql
        return chartVO;

    }

    public DashboardVO dashboard2VO(Dashboard dashboard, List<ChartVO> charts) {
        DashboardSimpleVO simpleVO = DashboardSimpleVO.toSimpleVO(dashboard);
        DashboardVO vo = new DashboardVO();
        vo
                .setDashboardId(simpleVO.getRefId())
                .setName(simpleVO.getName())
                .setRemarks(simpleVO.getRemarks())
                .setCreateUser(simpleVO.getCreateUser())
                .setModifyUser(simpleVO.getModifyUser())
        ;
        vo.setLayoutConfig(JSON.parseObject(dashboard.getLayoutConfig(), LayoutConfig.class));

        List<DashboardFilter> dashboardFilters = dashboardFilterDao.getListByDashboardId(dashboard.getId());
        List<DashboardFilterVO> dashboardFilterVOList = new ArrayList<>();
        for (DashboardFilter dashboardFilter : dashboardFilters) {
            List<DashboardFilterSelected> dashboardFilterSelectedList = dashboardFilterSelectedDao.getListByDashboardFilterId(dashboardFilter.getId());
            dashboardFilterVOList.add(DashboardFilterVO.toVO(dashboardFilter, dashboardFilterSelectedList));
        }

        List<DashboardFilterVO> filters = DashboardFilterVO.toTreeList(dashboardFilterVOList);

        List<DashboardLink> dashboardLinkList = dashboardLinkDao.getListByDashboardId(dashboard.getId());
        List<ChartLinkVO> chartLinkList = ChartLinkVO.toTreeList(dashboard.getId(), dashboardLinkList);

        vo.setGlobalFilters(filters);
        vo.setCharts(charts);
        vo.setChartLinkList(chartLinkList);

        return vo;
    }
}
