package cn.shuangbofu.clairvoyance.web.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * Created by shuangbofu on 2020/8/5 16:33
 */
@Data
@Accessors(chain = true)
public class PreviewResult {
    private List<Map<String, Object>> data;
    private long total;

    public PreviewResult(List<Map<String, Object>> data, long total) {
        this.data = data;
        this.total = total;
    }
}
