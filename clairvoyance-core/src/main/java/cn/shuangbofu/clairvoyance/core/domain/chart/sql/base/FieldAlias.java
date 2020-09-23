package cn.shuangbofu.clairvoyance.core.domain.chart.sql.base;

import cn.shuangbofu.clairvoyance.core.domain.field.AbstractChartField;
import cn.shuangbofu.clairvoyance.core.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by shuangbofu on 2020/8/1 11:29
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class FieldAlias extends AbstractChartField {

    protected String aliasName;
    private String description;

    @Override
    public String getRealAliasName() {
        String title = getRealAliasName0();
        String aliasName = clearSymbol(this.aliasName);
        if (StringUtils.isNotEmpty(aliasName)) {
            title = aliasName;
        }
        return title;
    }

    @JsonIgnore
    public String getQueryFinalName() {
        String queryName = getRealName();
        String finalAliasName = getRealAliasName();
        if (StringUtils.isNotEmpty(finalAliasName) && StringUtils.isNotEmpty(queryName)) {
            return String.format(" %s AS `%s` ", queryName, getRealAliasName());
        }
        return queryName;
    }

    @Override
    public String getName() {
        return getRealName();
    }

    protected String getRealAliasName0() {
        return super.getRealAliasName();
    }
}
