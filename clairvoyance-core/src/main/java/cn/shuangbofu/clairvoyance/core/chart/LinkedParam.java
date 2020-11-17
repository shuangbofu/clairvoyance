package cn.shuangbofu.clairvoyance.core.chart;

import lombok.Data;

import java.util.List;

/**
 * Created by shuangbofu on 2020/10/13 17:06
 */
@Data
@Deprecated
public class LinkedParam {
    private Long chartLinkId;
    private List<FieldValue> fieldValues;

    @Data
    static class FieldValue {
        private Long fieldId;
        private Object value;
    }
}
