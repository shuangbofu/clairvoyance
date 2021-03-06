package cn.shuangbofu.clairvoyance.core.field;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
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

    @Getter
    @Setter
    protected Long uniqId;
    @JsonIgnore
    private Field realField;
    @Setter
    private Long id;

    @Override
    public Long getUniqId() {
        return uniqId;
    }

    @JsonIgnore
    @Override
    public Field getRealField() {
        return realField;
    }

    public void setRealField(Field field) {
        realField = field;
    }

    @Override
    public void setRealFields(List<Field> fields) {
        Optional<Field> any = fields.stream().filter(i -> i.getId().equals(getId())).findAny();
        any.ifPresent(i -> setRealField(any.get()));
    }

    protected void setRealChartFields(List<Field> fields) {
        fields.forEach(field -> {
            if (field instanceof ChartField) {
                ChartField chartField = (ChartField) field;
                if (equal(chartField)) {
                    setRealField(chartField);
                }
            } else {
                if (field.getId().equals(getId())) {
                    setRealField(field);
                }
            }
        });
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
    public FieldType getFieldType() {
        return getValue(Field::getFieldType);
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

    @JsonIgnore
    @Override
    public String getFinalAliasName() {
        String realAliasName = getRealAliasName();
        if (realAliasName == null) {
            return null;
        }
        return String.format("%s(%s)", realAliasName, getUniqId());
    }
}
