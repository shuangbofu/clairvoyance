package cn.shuangbofu.clairvoyance.core.domain.chart;

import cn.shuangbofu.clairvoyance.core.domain.chart.sql.*;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.FieldAlias;
import cn.shuangbofu.clairvoyance.core.domain.worksheet.Field;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by shuangbofu on 2020/7/30 下午11:09
 */
@Data
@Accessors(chain = true)
public class Sql {

    /**
     * 筛选器
     */
    List<ChartFilter> filters;

    /**
     * 图内筛选器
     */
    List<ChartInnerFilter> innerFilters;

    /**
     * 维度，groupBy
     */
    List<Dimension> x;

    /**
     * 数值，select
     */
    List<Value> y;

    /**
     * 排序
     */
    Order order;

    /**
     * 对比和次轴后期可以在这里扩展
     */

    public static Sql defaultValue() {
        return new Sql().setFilters(new ArrayList<>())
                .setInnerFilters(new ArrayList<>())
                .setX(new ArrayList<>())
                .setY(new ArrayList<>())
                .setOrder(new Order());
    }

    public void updateFieldTitle(List<Field> fields) {
        updateFieldTitle0(x, fields);
        updateFieldTitle0(y, fields);
    }

    private <T extends FieldAlias> void updateFieldTitle0(List<T> fieldAliases, List<Field> fields) {
        for (FieldAlias alias : fieldAliases) {
            Optional<Field> any = fields.stream().filter(i -> i.getName().equals(alias.getName())).findAny();
            any.ifPresent(i -> alias.setTitle(i.getTitle()));
        }
    }
}
