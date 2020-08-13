package cn.shuangbofu.clairvoyance.web.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/13 下午10:02
 */
@Data
public class LayoutConfig {
    private List<Layout> positions;

    @Data
    static class Layout {
        private Integer x;
        private Integer y;
        private Integer w;
        private Integer h;
        @JsonProperty("i")
        private Long id;
    }
}
