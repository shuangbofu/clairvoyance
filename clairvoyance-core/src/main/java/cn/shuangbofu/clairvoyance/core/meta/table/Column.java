package cn.shuangbofu.clairvoyance.core.meta.table;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by shuangbofu on 2020/8/3 下午9:41
 */
@Data
@Accessors(chain = true)
public class Column {
    private String name;
    private String type;
    private String comment;
}
