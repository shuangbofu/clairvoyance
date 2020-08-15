package cn.shuangbofu.clairvoyance.core.domain.chart;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/15 17:53
 */
@Data
@Accessors(chain = true)
public class GlobalFilterParam {
    private List<String> range;
    private Long dashboardFilterId;
}
