package cn.shuangbofu.clairvoyance.core.domain.field;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by shuangbofu on 2020/8/6 14:40
 */
@Data
@Accessors(chain = true)
public abstract class Field {
    protected Long id;
    protected String title;
    protected String name;
    protected String description;
}
