package cn.shuangbofu.clairvoyance.core.chart.filter;

import cn.shuangbofu.clairvoyance.core.meta.utils.SqlUtil;
import cn.shuangbofu.clairvoyance.core.utils.StringUtils;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/8/8 下午8:45
 */
@Data
@Accessors(chain = true)
public class ExactChartFilter extends AbstractInnerChartFilter implements InnerChartFilter {
    public List<Object> range;
    public Boolean included;
    /**
     * 是否显示全部
     */
    private Boolean showAll;
    /**
     * 保存的下拉列表
     */
    private List<Object> templates;

    public ExactChartFilter() {

    }

    public ExactChartFilter(List<Object> range, Boolean included, Long refId) {
        this.range = range.stream().map(Object::toString).filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        this.included = included;
        setId(refId);
    }

    @Override
    public String where0() {
        if (range == null || range.size() == 0) {
            return null;
        }
        String values = range.stream().map(SqlUtil::standardValue).collect(Collectors.joining(", "));
        return " " + getRealName() + (!included ? " NOT" : "") + " IN ( " + values + ")";
    }

    @Override
    public void setupInner() {
        included = true;
    }
}
