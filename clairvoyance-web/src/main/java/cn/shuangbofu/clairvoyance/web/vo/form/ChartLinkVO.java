package cn.shuangbofu.clairvoyance.web.vo.form;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Created by shuangbofu on 2020/10/15 11:03
 */
@Data
@Accessors(chain = true)
public class ChartLinkVO {

    private Long chartId;
    private List<Link> links;

    static class Link {
        private Long linkedChartId;
        private List<FieldMapping> fieldMappings;

        @Data
        static class FieldMapping {
            private Long fieldId;
            private Long linkedFieldId;
        }
    }
}
