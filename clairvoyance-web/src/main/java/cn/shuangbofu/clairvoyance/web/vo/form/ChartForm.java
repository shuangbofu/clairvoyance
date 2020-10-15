package cn.shuangbofu.clairvoyance.web.vo.form;

import cn.shuangbofu.clairvoyance.core.chart.DrillParam;
import cn.shuangbofu.clairvoyance.core.chart.GlobalFilterParam;
import cn.shuangbofu.clairvoyance.core.chart.LinkedParam;
import cn.shuangbofu.clairvoyance.web.vo.ChartVO;
import lombok.Data;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/18 11:09
 */
@Data
public class ChartForm extends ChartVO {

    private DrillParam drillParam;

    private List<GlobalFilterParam> globalFilterParams;

    private LinkedParam linkedParam;
}
