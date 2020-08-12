package cn.shuangbofu.clairvoyance.core.domain.chart;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shuangbofu on 2020/8/12 15:59
 */
@Data
@Accessors(chain = true)
public class DrillParam {
    private int level;
    private List<Object> values;

    public static DrillParam empty() {
        return new DrillParam().setLevel(0).setValues(new ArrayList<>());
    }
}
