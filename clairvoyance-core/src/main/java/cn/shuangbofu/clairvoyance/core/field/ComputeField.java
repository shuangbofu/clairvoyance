package cn.shuangbofu.clairvoyance.core.field;

import lombok.Data;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/6 14:42
 * 自定义聚合计算字段
 */
@Data
public class ComputeField extends AbstractField {

    String formula;

    List<Long> fieldList;

    List<Field> fields;


    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getRealName() {
        String name = formula;
        for (int index = 0; index < fields.size(); index++) {
            name = name.replaceAll("\\{\\{" + index + "}}", " " + fields.get(index).getName() + " ");
        }
        return name;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }
}
