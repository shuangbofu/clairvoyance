package cn.shuangbofu.clairvoyance.core.domain.field;

import cn.shuangbofu.clairvoyance.core.enums.ColumnType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Setter;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Created by shuangbofu on 2020/8/10 11:49
 * 最终执行通用字段，写入库序列化时没有realField
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractChartField implements ChartField {

    @JsonIgnore
    private Field realField;

    @Setter
    private Long id;

    @Override
    public void setRealFields(List<Field> fields) {
        Optional<Field> any = fields.stream().filter(i -> i.getId().equals(getId())).findAny();
        any.ifPresent(i -> realField = any.get());
    }

    @Override
    public String getTitle() {
        return getValue(Field::getTitle);
    }

    @Override
    public String getName() {
        return getValue(Field::getName);
    }

    @JsonIgnore
    @Override
    public String getRealName() {
        return getValue(Field::getRealName);
    }

    @Override
    public String getRealAliasName() {
        return getValue(Field::getRealAliasName);
    }

    @Override
    public ColumnType getType() {
        return getValue(Field::getType);
    }

    @Override
    public Long getId() {
        return id;
    }

    protected String clearSymbol(String aliasName) {
        if (aliasName == null) {
            return null;
        }
        return aliasName.replace("`", "")
                .replace("\"", "")
                .replace("'", "");
    }

    @JsonIgnore
    public <T> T getValue(Function<Field, T> get) {
        if (realField == null) {
            return null;
        }
        return get.apply(realField);
    }
}
