package cn.shuangbofu.clairvoyance.web.vo;

import cn.shuangbofu.clairvoyance.core.db.Chart;
import cn.shuangbofu.clairvoyance.core.db.Field;
import cn.shuangbofu.clairvoyance.core.domain.chart.AlarmConfig;
import cn.shuangbofu.clairvoyance.core.domain.chart.ChartSql;
import cn.shuangbofu.clairvoyance.core.enums.ChartType;
import cn.shuangbofu.clairvoyance.core.loader.FieldLoader;
import cn.shuangbofu.clairvoyance.core.utils.JSON;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Created by shuangbofu on 2020/7/30 下午11:08
 * <p>
 * 需要图表配置信息
 * {
 * tableID: xx,
 * x:[],
 * y:[],
 * config: {}
 * dashboardId:xxx
 * alarmconfig: {}
 * }
 */
@Data
@Accessors(chain = true)
public class ChartVO {

    private Long chartId;
    private ChartType chartType;
    private Long dashboardId;
    private Long workSheetId;
    private String layoutConfig;
    private AlarmConfig alarmConfig;
    private ChartSql sqlConfig;
    private String name;

    public static ChartVO toVO(Chart chart) {
        // fields更新到sql
        List<Field> fields = FieldLoader.getOriginFields(chart.getWorkSheetId());
        ChartSql sql = JSON.parseObject(chart.getSqlConfig(), ChartSql.class);
        sql.setFields(fields);

        return new ChartVO()
                .setChartType(chart.getChartType())
                .setName(chart.getName())
                .setChartId(chart.getId())
                .setDashboardId(chart.getDashboardId())
                .setWorkSheetId(chart.getWorkSheetId())
                .setLayoutConfig(chart.getLayoutConfig())
                .setAlarmConfig(JSON.parseObject(chart.getAlarmConfig(), AlarmConfig.class))
                .setWorkSheetId(chart.getWorkSheetId())
                .setSqlConfig(sql)
                ;
    }

    public Chart toModel() {
        Chart chart = new Chart()
                .setChartType(chartType)
                .setName(name)
                .setId(chartId)
                .setDashboardId(dashboardId)
                .setWorkSheetId(workSheetId);

        if (!created()) {
//            layoutConfig = new JSONObject();
            alarmConfig = new AlarmConfig();
            sqlConfig = ChartSql.defaultValue();
        } else {
            List<Field> fields = FieldLoader.getOriginFields(chart.getWorkSheetId());
            sqlConfig.setFields(fields);
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
