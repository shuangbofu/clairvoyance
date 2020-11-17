package cn.shuangbofu.clairvoyance.web.pojo.Model.Chart;

import cn.shuangbofu.clairvoyance.core.chart.AlarmConfig;
import cn.shuangbofu.clairvoyance.core.chart.ChartSql;
import cn.shuangbofu.clairvoyance.core.utils.JSON;
import cn.shuangbofu.clairvoyance.web.entity.Chart;
import cn.shuangbofu.clairvoyance.web.vo.ChartLayoutConfig;
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
public class ChartModel {

    private Long chartId;
    private Long dashboardId;
    private Long workSheetId;
    private List<ChartLayoutConfig> layoutConfigs;
    private AlarmConfig alarmConfig;
    private ChartSql sqlConfig;
    private String name;
    private String remarks;
    //    private User createUser;
//    private User modifyUser;
    private List<Long> linkComputeFields;

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
