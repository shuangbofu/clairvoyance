package cn.shuangbofu.clairvoyance.core.domain.field;

import cn.shuangbofu.clairvoyance.core.enums.ColumnType;
import cn.shuangbofu.clairvoyance.core.utils.JSON;
import cn.shuangbofu.clairvoyance.core.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/8/6 14:40
 */
@Data
@Accessors(chain = true)
public abstract class Field {
    protected Long id;
    protected String title;
    protected ColumnType type;
    protected String name;
    protected String description;

    public static List<Field> fromDb(List<cn.shuangbofu.clairvoyance.core.db.Field> fields) {
        return fields.stream().map(i -> Field.fromDb(fields, i)).collect(Collectors.toList());
    }

    public static Field fromDb(List<cn.shuangbofu.clairvoyance.core.db.Field> fields, cn.shuangbofu.clairvoyance.core.db.Field field) {
        Field finalField = null;
        String config = field.getConfig();
        switch (field.getFieldType()) {
            case origin:
                finalField = new OriginField();
                break;
            case group:
                GroupField groupField = JSON.parseObject(config, GroupField.class);
                Optional<cn.shuangbofu.clairvoyance.core.db.Field> any = fields.stream().filter(i -> i.getId().equals(groupField.getRefId())).findAny();
                any.ifPresent(value -> groupField.setRefField(fromDb(fields, value)));
                finalField = groupField;
                break;
            case computed:
                ComputeField computeField = JSON.parseObject(config, ComputeField.class);
                computeField.setFields(fromDb(fields));
                finalField = computeField;
                break;
            default:
                break;
        }
        if (finalField != null) {
            return finalField.setId(field.getId())
                    .setName(field.getName())
                    .setTitle(field.getTitle())
                    .setType(field.getColumnType());
        }
        return null;
    }

    @JsonIgnore
    public String getName() {
        return name;
    }

    @JsonIgnore
    public String getAliasName() {
        String title = this.title;
        if (StringUtils.isNotEmpty(title)) {
            title = name;
        }
        return title;
    }

    @JsonIgnore
    public String getRealName() {
        return getName();
    }
}
