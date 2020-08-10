package cn.shuangbofu.clairvoyance.core.domain.field;

import cn.shuangbofu.clairvoyance.core.domain.field.group.ConditionGroupField;
import cn.shuangbofu.clairvoyance.core.domain.field.group.CustomStepGroupField;
import cn.shuangbofu.clairvoyance.core.domain.field.group.ExpressionGroupField;
import cn.shuangbofu.clairvoyance.core.domain.field.group.FixedStepGroupField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/6 14:42
 * 自定义分组字段
 */
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "mode", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ConditionGroupField.class, name = GroupField.GroupMode.CONDITION),
        @JsonSubTypes.Type(value = ExpressionGroupField.class, name = GroupField.GroupMode.EXPRESSION),
        @JsonSubTypes.Type(value = FixedStepGroupField.class, name = GroupField.GroupMode.FIXED_STEP),
        @JsonSubTypes.Type(value = CustomStepGroupField.class, name = GroupField.GroupMode.CUSTOM_STEP)
})
public abstract class GroupField extends Field {
    @JsonIgnore
    protected Field refField;
    private String mode;
    private Long refId;

    public abstract List<String> getRange();

    public interface GroupMode {
        String CONDITION = "condition";
        String EXPRESSION = "expression";
        String FIXED_STEP = "fixed_step";
        String CUSTOM_STEP = "custom_step";
    }
}
