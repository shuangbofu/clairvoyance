package cn.shuangbofu.clairvoyance.core.field.group;

import cn.shuangbofu.clairvoyance.core.field.GroupField;
import lombok.Data;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/7 16:03
 */
@Data
public class ConditionGroupField extends GroupField {
    @Override
    public List<String> getRange() {
        return null;
    }
}
