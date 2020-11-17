package cn.shuangbofu.clairvoyance.web.vo.form;

import cn.shuangbofu.clairvoyance.core.chart.field.AggregatorFunc;
import cn.shuangbofu.clairvoyance.core.chart.filter.ChartFilter;
import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * Created by shuangbofu on 2020/8/6 11:50
 */
@Data
public class RangeRequestForm {

    private Long workSheetId;
    private Long fieldId;
    private List<ChartFilter> filters;
    private AggregatorFunc aggregator;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        cn.shuangbofu.clairvoyance.web.vo.form.RangeRequestForm that = (cn.shuangbofu.clairvoyance.web.vo.form.RangeRequestForm) o;
        return Objects.equals(workSheetId, that.workSheetId) &&
                Objects.equals(fieldId, that.fieldId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workSheetId, fieldId);
    }
}
