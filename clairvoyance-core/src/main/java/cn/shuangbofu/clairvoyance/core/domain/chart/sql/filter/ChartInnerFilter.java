package cn.shuangbofu.clairvoyance.core.domain.chart.sql.filter;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by shuangbofu on 2020/8/1 11:15
 */
@Data
public class ChartInnerFilter extends ExactChartFilter {

    /**
     * 是否显示全部
     */
    private Boolean showAll;
    /**
     * 保存的下拉列表
     */
    private List<Object> templates;

    @Override
    public String where() {
        included = true;
        return super.where();
    }

    public Boolean getShowAll() {
        return Optional.ofNullable(showAll).orElse(false);
    }

    public List<Object> getTemplates() {
        return Optional.ofNullable(templates).orElse(new ArrayList<>());
    }
}
