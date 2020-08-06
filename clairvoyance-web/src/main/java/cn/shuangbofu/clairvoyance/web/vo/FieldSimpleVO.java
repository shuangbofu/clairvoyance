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
public class FieldSimpleVO {

    private Long id;

    private String name;

    private String title;

    private ColumnType type;

    private String remarks;

    private Integer seqNo;

    private String linkUrl;

    public static List<FieldSimpleVO> toVOs(List<Field> fields) {
        return fields.stream().map(FieldSimpleVO::toVO).collect(Collectors.toList());
    }

    public static FieldSimpleVO toVO(Field field) {
        return new FieldSimpleVO().setId(field.getId())
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
