package cn.shuangbofu.clairvoyance.core.db;

import cn.shuangbofu.clairvoyance.core.enums.ColumnType;
import cn.shuangbofu.clairvoyance.core.enums.FieldType;
import cn.shuangbofu.clairvoyance.core.utils.StringUtils;
import io.github.biezhi.anima.annotation.Table;
import io.github.biezhi.anima.core.AnimaQuery;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by shuangbofu on 2020/8/5 11:59
 */
@Data
@Accessors(chain = true)
@Table(name = "work_sheet_field")
public class Field extends Model<Field> {

    private Long id;

    private Long gmtCreate;
    private Long gmtModified;

    private String name;

    private String title;

    private ColumnType columnType;

    private FieldType fieldType;

    private String remarks;

    private int seqNo;

    private String linkUrl;

    private String config;

    private Long workSheetId;

    public static AnimaQuery<Field> from() {
        return s(Field.class);
    }

    /**
     * 同步表结构生成字段
     *
     * @param name
     * @param comment
     * @return
     */
    public static Field newColumn(String name, String comment, String type) {
        return new Field().setName(name).setTitle(
                StringUtils.emptyGet(comment, name)
        ).setRemarks(comment)
                .setColumnType(ColumnType.valueOfType(type))
                .setFieldType(FieldType.origin);
    }
}
