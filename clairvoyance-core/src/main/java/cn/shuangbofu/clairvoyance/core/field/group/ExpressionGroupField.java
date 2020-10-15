package cn.shuangbofu.clairvoyance.core.field.group;

import cn.shuangbofu.clairvoyance.core.field.GroupField;
import cn.shuangbofu.clairvoyance.core.meta.utils.SqlUtil;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/8/6 15:54
 * <p>
 * (CASE
 * WHEN (ref_id!=0) THEN '是'
 * WHEN (ref_id=0) THEN '否'
 * ELSE '未分组'
 * END);
 */
@Data
public class ExpressionGroupField extends GroupField {
    private static final String NO_GROUP = "未分组";
    private List<Case> groups;

    @Override
    public List<String> getRange() {
        List<String> ranges = Lists.newArrayList(NO_GROUP);
        ranges.addAll(groups.stream().map(Case::getName).collect(Collectors.toList()));
        return ranges;
    }

    @Override
    public String getRealName() {
        String cases = groups.stream().map(i -> {
            String expression = i.getExpression();
            expression = SqlUtil.standardWhereSql(expression);
            expression = expression.replace("[" + refField.getTitle() + "]", refField.getName());
            return String.format("WHEN %s THEN '%s'", expression, i.getName());
        }).collect(Collectors.joining(" "));
        return String.format("(CASE %s ELSE '%s' END)", cases, NO_GROUP);
    }

    @Data
    static class Case {
        private String name;
        private String expression;
    }
}
