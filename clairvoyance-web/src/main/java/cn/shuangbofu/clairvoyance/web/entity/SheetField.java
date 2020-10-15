package cn.shuangbofu.clairvoyance.web.entity;

import cn.shuangbofu.clairvoyance.core.utils.StringUtils;
import cn.shuangbofu.clairvoyance.web.enums.ColumnType;
import cn.shuangbofu.clairvoyance.web.enums.FieldType;
import io.github.biezhi.anima.annotation.Column;
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
public class SheetField extends Model<SheetField> {

    private Long id;

    private Long gmtCreate;
    private Long gmtModified;
    @Column(name = "`status`")
    private Boolean deleted;

    private String name;

    private String title;

    private ColumnType columnType;

    private FieldType fieldType;

    private String remarks;

    private int seqNo;

    private String linkUrl;

    private String config;

    private Long workSheetId;

    public static AnimaQuery<SheetField> from() {
        return s(SheetField.class).where(SheetField::getDeleted, 0);
    }

    /**
     * 同步表结构生成字段
     *
     * @param name
     * @param comment
     * @return
     */
    public static SheetField newColumn(String name, String comment, String type) {
        return new SheetField().setName(name).setTitle(
                StringUtils.emptyGet(comment, name)
        ).setRemarks(comment)
                .setColumnType(ColumnType.valueOfType(type))
                .setFieldType(FieldType.origin);
    }
}
