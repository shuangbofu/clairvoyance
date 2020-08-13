package cn.shuangbofu.clairvoyance.web.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Comparator;
import java.util.List;

/**
 * Created by shuangbofu on 2020/8/13 下午10:02
 */
@Data
public class LayoutConfig {
    private List<Layout> positions;

    @JsonIgnore
    public int getMaxBottom() {
        return positions.stream().map(Layout::getBottom).max(Comparator.comparingInt(a -> a)).orElse(0);
    }

    @Data
    public static class Layout {
        private Integer x;
        private Integer y;
        private Integer w;
        private Integer h;
        @JsonProperty("i")
        private Long id;

        public Layout() {
        }

        public Layout(int y, Long id) {
            this.id = id;
            this.y = y;
            x = 0;
            w = 4;
            h = 10;
        }

        @JsonIgnore
        public int getBottom() {
            return y + h;
        }
    }
}
