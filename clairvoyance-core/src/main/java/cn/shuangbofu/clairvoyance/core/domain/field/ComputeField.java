package cn.shuangbofu.clairvoyance.core.domain.field;

import lombok.Data;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/6 14:42
 * 自定义聚合计算字段
 */
@Data
public class ComputeField extends AbstractField {

    String formula;

    @Override
    public String getName() {
//        formula.replace()
        return "";
    }

    @Override
    public String getRealName() {

        return null;
    }

    public void setFields(List<Field> fields) {

    }
}
