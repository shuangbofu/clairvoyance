package cn.shuangbofu.clairvoyance.core.domain.field;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/10 20:16
 */
public interface ChartField {

    String getName();

    Long getId();

    String getTitle();

    String getRealName();

    String getRealAliasName();

    void setRealFields(List<Field> fields);
}
