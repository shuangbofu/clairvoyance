package cn.shuangbofu.clairvoyance.core.field;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/10 20:16
 */
public interface ChartField extends Field {

    @JsonIgnore
    Field getRealField();

    String getFinalAliasName();

    Long getUniqId();

    void setRealFields(List<Field> fields);

    default void ifNull(Runnable runnable) {
        if (getRealField() == null) {
            runnable.run();
        }
    }

    default boolean equal(ChartField chartField) {
        Long uniqId = getUniqId();
        return uniqId != null && uniqId.equals(chartField.getUniqId());
    }
}
