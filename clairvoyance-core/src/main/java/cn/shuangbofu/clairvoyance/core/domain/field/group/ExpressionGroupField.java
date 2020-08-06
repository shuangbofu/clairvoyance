package cn.shuangbofu.clairvoyance.core.domain.field.group;

import cn.shuangbofu.clairvoyance.core.domain.field.GroupField;
import lombok.Data;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/6 15:54
 */
@Data
public class ExpressionGroupField extends GroupField {
    @Override
    public List<Object> getRange() {
        return null;
    }
}
