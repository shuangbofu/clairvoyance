package cn.shuangbofu.clairvoyance.core.domain.field;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/10 20:16
 */
public interface ChartField extends Field {

    void setRealFields(List<Field> fields);
}
