package cn.shuangbofu.clairvoyance.web.vo;

import cn.shuangbofu.clairvoyance.core.db.Chart;
import cn.shuangbofu.clairvoyance.core.domain.chart.AlarmConfig;
import cn.shuangbofu.clairvoyance.core.domain.chart.ChartSql;
import cn.shuangbofu.clairvoyance.core.domain.chart.ChartSqlBuilder;
import cn.shuangbofu.clairvoyance.core.utils.JSON;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/7/30 下午11:08
 * <p>
 * 需要图表配置信息
 */
@Data
@Accessors(chain = true)
public class ChartVO {

    private Long chartId;
    private Long dashboardId;
    private Long workSheetId;
    private List<ChartLayoutConfig> layoutConfigs;
    private AlarmConfig alarmConfig;
    private ChartSql sqlConfig;
    private String name;

    public static ChartVO toVO(Chart chart) {
        ChartSql sqlConfig = new ChartSqlBuilder(chart.getSqlConfig(), chart.getWorkSheetId()).build();
        List<ChartLayoutConfig> configs = JSON.parseArray(chart.getLayoutConfig(), ChartLayoutConfig.class);

        if (configs == null || configs.size() == 0) {
            configs = sqlConfig.getLayers().stream().map(i -> new ChartLayoutConfig().setChartType(i.getChartType())).collect(Collectors.toList());
        }
        // fields更新到sql
        return new ChartVO()
                .setName(chart.getName())
                .setChartId(chart.getId())
                .setDashboardId(chart.getDashboardId())
                .setWorkSheetId(chart.getWorkSheetId())
                .setLayoutConfigs(configs)
                .setAlarmConfig(JSON.parseObject(chart.getAlarmConfig(), AlarmConfig.class))
                .setWorkSheetId(chart.getWorkSheetId())
                .setSqlConfig(sqlConfig);
    }

    public Chart toModel() {
        Chart chart = new Chart()
                .setName(name)
                .setId(chartId)
                .setDashboardId(dashboardId)
                .setWorkSheetId(workSheetId);

        if (!created()) {
            layoutConfigs = new ArrayList<>();
            alarmConfig = new AlarmConfig();
            sqlConfig = ChartSql.defaultValue();
        }

        chart
                .setLayoutConfig(JSON.toJSONString(layoutConfigs))
                .setAlarmConfig(JSON.toJSONString(alarmConfig))
                .setSqlConfig(sqlConfig.toJSONString());
        return chart;
    }

    public boolean created() {
        return chartId != null && chartId != 0L;
    }
}
