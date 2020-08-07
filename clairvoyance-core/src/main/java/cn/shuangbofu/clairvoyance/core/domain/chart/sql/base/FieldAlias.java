package cn.shuangbofu.clairvoyance.core.domain.chart.sql.base;

import cn.shuangbofu.clairvoyance.core.domain.field.Field;
import cn.shuangbofu.clairvoyance.core.utils.StringUtils;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Created by shuangbofu on 2020/8/1 11:29
 */
@Data
public abstract class FieldAlias extends Field {

    protected String aliasName;

    @JSONField(serialize = false, deserialize = false)
    public String getFinalTitle() {
        return title;
    }

    public String getFinalAliasName() {
        String title = getFinalTitle();
        String aliasName = clearSymbol(this.aliasName);
        if (StringUtils.isNotEmpty(aliasName)) {
            title = aliasName;
        }
        return title;
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

    @JSONField(serialize = false, deserialize = false)
    public boolean isValid() {
        return StringUtils.isNotEmpty(name);
    }

    private String clearSymbol(String aliasName) {
        if (aliasName == null) {
            return null;
        }
        return aliasName.replace("`", "")
                .replace("\"", "")
                .replace("'", "");
    }
}