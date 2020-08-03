package cn.shuangbofu.clairvoyance.core.domain.chart.sql.base;

import cn.shuangbofu.clairvoyance.core.utils.StringUtils;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Created by shuangbofu on 2020/8/1 11:29
 */
@Data
public abstract class FieldAlias {

    protected String title;
    protected String name;
    protected String aliasName;
    protected String description;

    @JSONField(serialize = false, deserialize = false)
    public String getFinalTitle() {
        return title;
    }

    public String getFinalAliasName() {
        String aliasName = getFinalTitle();
        if (StringUtils.isNotEmpty(this.aliasName)) {
            aliasName = this.aliasName;
        }
        return aliasName;
    }

    @JSONField(serialize = false, deserialize = false)
    public String getQueryFinalName() {
        String queryName = getQueryName();
        String finalAliasName = getFinalAliasName();
        if (StringUtils.isNotEmpty(finalAliasName)) {
            return String.format(" %s AS `%s` ", queryName, finalAliasName);
        }
        return queryName;
    }

    @JSONField(serialize = false, deserialize = false)
    public String getQueryName() {
        return name;
    }

    public boolean isValid() {
        return StringUtils.isNotEmpty(name);
    }
}
