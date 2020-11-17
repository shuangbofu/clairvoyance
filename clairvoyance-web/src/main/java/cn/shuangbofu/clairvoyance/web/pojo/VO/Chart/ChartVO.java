package cn.shuangbofu.clairvoyance.web.pojo.VO.Chart;

import cn.shuangbofu.clairvoyance.core.chart.AlarmConfig;
import cn.shuangbofu.clairvoyance.core.chart.ChartSql;
import cn.shuangbofu.clairvoyance.web.service.User;
import cn.shuangbofu.clairvoyance.web.vo.ChartLayoutConfig;
import cn.shuangbofu.clairvoyance.web.vo.FieldSimpleVO;
import lombok.Data;

import java.util.List;

/**
 * @Date: 2020/11/11 4:37 下午
 */
@Data
public class ChartVO {

    private Long chartId;
    private Long dashboardId;
    private Long workSheetId;
    private List<ChartLayoutConfig> layoutConfigs;
    private AlarmConfig alarmConfig;
    private ChartSql sqlConfig;
    private String name;
    private String remarks;
    private User createUser;
    private User modifyUser;
    private List<FieldSimpleVO> computeFields;
    private Boolean hasChartLink = false;

}
