package cn.shuangbofu.clairvoyance.web.vo;

import cn.shuangbofu.clairvoyance.core.enums.ChartType;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by shuangbofu on 2020/8/19 下午2:48
 */
@Data
@Accessors(chain = true)
public class ChartLayoutConfig {
    private ChartType chartType;
    private JsonNode chartStyle;
    private ChartType optionalChartType;
}
