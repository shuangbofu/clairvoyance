package cn.shuangbofu.clairvoyance.core.field.group;

import cn.shuangbofu.clairvoyance.core.field.GroupField;
import lombok.Data;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/6 15:56
 */
@Data
public class FixedStepGroupField extends GroupField {

    @Override
    public List<String> getRange() {
        return null;
    }
}
