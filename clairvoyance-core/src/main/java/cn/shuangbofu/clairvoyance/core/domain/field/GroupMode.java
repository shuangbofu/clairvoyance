package cn.shuangbofu.clairvoyance.core.domain.field;

import cn.shuangbofu.clairvoyance.core.domain.field.group.ConditionGroupField;
import cn.shuangbofu.clairvoyance.core.domain.field.group.CustomStepGroupField;
import cn.shuangbofu.clairvoyance.core.domain.field.group.ExpressionGroupField;
import cn.shuangbofu.clairvoyance.core.domain.field.group.FixedStepGroupField;
import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;

/**
 * Created by shuangbofu on 2020/8/6 14:46
 */
@AllArgsConstructor
public enum GroupMode {

    /**
     * 分组模式
     * 表达式，固定步长，自定义步长
     */
    condition(ConditionGroupField.class),
    expression(ExpressionGroupField.class),
    fixed_step(CustomStepGroupField.class),
    custom_step(FixedStepGroupField.class),

    ;

    private final Class<? extends GroupField> groupClass;


    public GroupField getGroup(String config) {
        return JSON.parseObject(config, groupClass);
    }
}
