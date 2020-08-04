package cn.shuangbofu.clairvoyance.core.domain.worksheet;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by shuangbofu on 2020/7/30 下午10:23
 */
@Data
@Accessors(chain = true)
public class SourceConfig {
    private String tableName;
    private String jdbcUrl;
    private String username;
    private String password;

    public SourceConfig() {

    }

    public static SourceConfig getSourceConfig(String str) {
        return JSON.parseObject(str, SourceConfig.class);
    }
}
