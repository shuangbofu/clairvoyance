package cn.shuangbofu.clairvoyance.core.domain.chart.sql.filter;

import cn.shuangbofu.clairvoyance.core.domain.chart.sql.Value;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

/**
 * Created by shuangbofu on 2020/9/17 下午11:36
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "filterType", visible = true)
public interface InnerChartFilter extends ChartFilter {
    /**
     * innerFilter的特殊处理
     */
    void setupInner();

    void setValues(List<Value> values);
}
