package cn.shuangbofu.clairvoyance.web.vo;

import cn.shuangbofu.clairvoyance.core.db.Chart;
import cn.shuangbofu.clairvoyance.core.domain.chart.AlarmConfig;
import cn.shuangbofu.clairvoyance.core.domain.chart.ChartSql;
import cn.shuangbofu.clairvoyance.core.domain.chart.SqlBuiler;
import cn.shuangbofu.clairvoyance.core.utils.JSON;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by shuangbofu on 2020/7/30 下午11:08
 * <p>
 * 需要图表配置信息
 */
@Data
@Accessors(chain = true)
public class ChartVO {

    private Long chartId;
    //    private ChartType chartType;
    private Long dashboardId;
    private Long workSheetId;
    private String layoutConfig;
    private AlarmConfig alarmConfig;
    private ChartSql sqlConfig;
    private String name;

    public static ChartVO toVO(Chart chart) {
        // fields更新到sql
        return new ChartVO()
//                .setChartType(chart.getChartType())
                .setName(chart.getName())
                .setChartId(chart.getId())
                .setDashboardId(chart.getDashboardId())
                .setWorkSheetId(chart.getWorkSheetId())
                .setLayoutConfig(chart.getLayoutConfig())
                .setAlarmConfig(JSON.parseObject(chart.getAlarmConfig(), AlarmConfig.class))
                .setWorkSheetId(chart.getWorkSheetId())
                .setSqlConfig(SqlBuiler.buildChartSql(chart.getSqlConfig(), chart.getWorkSheetId()));
    }

    public Chart toModel() {
        Chart chart = new Chart()
//                .setChartType(chartType)
                .setName(name)
                .setId(chartId)
                .setDashboardId(dashboardId)
                .setWorkSheetId(workSheetId);

        if (!created()) {
//            layoutConfig = new JSONObject();
            alarmConfig = new AlarmConfig();
            sqlConfig = ChartSql.defaultValue();
        }

        chart
//                .setLayoutConfig(layoutConfig.toString())
                .setAlarmConfig(JSON.toJSONString(alarmConfig))
                .setSqlConfig(sqlConfig.toJSONString());
        // TODO 从config中解析type？ type有什么用？
//                .setChartType()
        return chart;
    }

    public boolean created() {
        return chartId != null && chartId != 0L;
    }
}
