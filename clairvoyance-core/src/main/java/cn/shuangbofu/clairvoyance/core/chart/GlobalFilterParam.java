package cn.shuangbofu.clairvoyance.core.chart;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/15 17:53
 */
@Data
@Accessors(chain = true)
//@Deprecated
public class GlobalFilterParam {
    private List<Object> range;
    private Long dashboardFilterId;
    private Boolean included;
}
