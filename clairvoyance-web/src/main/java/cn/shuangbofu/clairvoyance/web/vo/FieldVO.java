package cn.shuangbofu.clairvoyance.web.vo;

import cn.shuangbofu.clairvoyance.core.db.Field;
import cn.shuangbofu.clairvoyance.core.enums.ColumnType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/8/5 14:06
 */
@Data
@Accessors(chain = true)
public class FieldVO {

    private Long id;

    private String name;

    private String title;

    private ColumnType type;

    private String remarks;

    private int seqNo;

    private String linkUrl;

    public static List<FieldVO> toVOs(List<Field> fields) {
        return fields.stream().map(FieldVO::toVO).collect(Collectors.toList());
    }

    public static FieldVO toVO(Field field) {
        return new FieldVO().setId(field.getId())
                .setName(field.getName())
                .setTitle(field.getTitle())
                .setType(field.getColumnType())
                .setRemarks(field.getRemarks())
                .setSeqNo(field.getSeqNo())
                .setLinkUrl(field.getLinkUrl());
    }

    public Field toModel() {
        return new Field().setId(id).setTitle(title).setColumnType(type).setRemarks(remarks).setLinkUrl(linkUrl);
    }
}
