package cn.shuangbofu.clairvoyance.core.domain.field;

import lombok.Data;

/**
 * Created by shuangbofu on 2020/8/6 14:40
 */
@Data
public abstract class Field {
    protected Long id;
    protected String title;
    protected String name;
    protected String description;
}
