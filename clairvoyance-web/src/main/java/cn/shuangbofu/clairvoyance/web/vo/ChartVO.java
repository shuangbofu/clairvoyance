package cn.shuangbofu.clairvoyance.web.vo;

import cn.shuangbofu.clairvoyance.core.db.Chart;
import cn.shuangbofu.clairvoyance.core.domain.chart.AlarmConfig;
import cn.shuangbofu.clairvoyance.core.domain.chart.ChartLayer;
import cn.shuangbofu.clairvoyance.core.domain.chart.ChartSql;
import cn.shuangbofu.clairvoyance.core.domain.chart.ChartSqlBuilder;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.Value;
import cn.shuangbofu.clairvoyance.core.enums.ChartType;
import cn.shuangbofu.clairvoyance.core.utils.JSON;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

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
    private String remarks;

    public static ChartVO toVO(Chart chart) {
        ChartSql sqlConfig = new ChartSqlBuilder(chart.getSqlConfig(), chart.getWorkSheetId()).build();
        List<ChartLayoutConfig> configs = JSON.parseArray(chart.getLayoutConfig(), ChartLayoutConfig.class);
        // 只有表格才有行总计
        for (int i = 0; i < sqlConfig.getLayers().size(); i++) {
            ChartLayer layer = sqlConfig.getLayers().get(i);
            ChartLayoutConfig layoutConfig = configs.get(i);
            if (!ChartType.C1.equals(layoutConfig.getChartType())) {
                layer.getY().removeIf(Value::total);
            }
        }
        // fields更新到sql
        return new ChartVO()
                .setName(chart.getName())
                .setRemarks(chart.getRemarks())
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
                .setRemarks(remarks)
                .setName(name)
                .setId(chartId)
                .setDashboardId(dashboardId)
                .setWorkSheetId(workSheetId);

        if (!created()) {
            if (layoutConfigs == null) {
                layoutConfigs = new ArrayList<>();
                layoutConfigs.add(new ChartLayoutConfig());
            }
            if (alarmConfig == null) {
                alarmConfig = new AlarmConfig();
            }
            if (sqlConfig == null) {
                sqlConfig = ChartSql.defaultValue();
            }
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
