package cn.shuangbofu.clairvoyance.web.vo.form;

import cn.shuangbofu.clairvoyance.core.chart.DrillParam;
import cn.shuangbofu.clairvoyance.web.pojo.Model.Chart.ChartModel;
import cn.shuangbofu.clairvoyance.web.pojo.Model.Chart.ChartQueryDataSearchParamsModel;
import lombok.Data;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/18 11:09
 */
@Data
public class ChartForm extends ChartModel {

    private DrillParam drillParam;

    private List<ChartQueryDataSearchParamsModel> searchParams;

}
