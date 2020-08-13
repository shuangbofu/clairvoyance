package cn.shuangbofu.clairvoyance.core.domain.field;

import cn.shuangbofu.clairvoyance.core.enums.ColumnType;
import cn.shuangbofu.clairvoyance.core.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by shuangbofu on 2020/8/13 18:07
 */
@Data
@Accessors(chain = true)
public abstract class AbstractField implements Field {
    protected Long id;
    protected String title;
    protected ColumnType type;
    protected String name;
    protected String description;

    @Override
    @JsonIgnore
    public String getRealAliasName() {
        return StringUtils.emptyGet(title, name);
    }

    @Override
    @JsonIgnore
    public String getRealName() {
        return getName();
    }
}
