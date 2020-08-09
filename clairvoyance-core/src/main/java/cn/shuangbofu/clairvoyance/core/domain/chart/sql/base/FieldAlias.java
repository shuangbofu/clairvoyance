package cn.shuangbofu.clairvoyance.core.domain.chart.sql.base;

import cn.shuangbofu.clairvoyance.core.domain.field.Field;
import cn.shuangbofu.clairvoyance.core.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by shuangbofu on 2020/8/1 11:29
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class FieldAlias extends Field {

    protected String aliasName;

    @JsonIgnore
    public String getFinalTitle() {
        return title;
    }

    @JsonProperty("finalAliasName")
    public String getFinalAliasName() {
        String title = getFinalTitle();
        String aliasName = clearSymbol(this.aliasName);
        if (StringUtils.isNotEmpty(aliasName)) {
            title = aliasName;
        }
        return title;
    }

    @JsonIgnore
    public String getQueryFinalName() {
        String queryName = getQueryName();
        String finalAliasName = getFinalAliasName();
        if (StringUtils.isNotEmpty(finalAliasName)) {
            return String.format(" %s AS `%s` ", queryName, finalAliasName);
        }
        return queryName;
    }

    @JsonIgnore
    public String getQueryName() {
        return name;
    }

    @JsonIgnore
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
