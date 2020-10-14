package cn.shuangbofu.clairvoyance.web.vo.form;

import cn.shuangbofu.clairvoyance.core.domain.chart.DrillParam;
import cn.shuangbofu.clairvoyance.core.domain.chart.GlobalFilterParam;
import cn.shuangbofu.clairvoyance.core.domain.chart.LinkedParam;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/15 17:47
 */
@Data
@Accessors(chain = true)
public class ChartParam {

    private DrillParam drillParam;

    private List<GlobalFilterParam> globalFilterParams;

    private LinkedParam linkedParam;
}
