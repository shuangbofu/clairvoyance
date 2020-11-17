package cn.shuangbofu.clairvoyance.web.vo;

import cn.shuangbofu.clairvoyance.core.utils.JSON;
import cn.shuangbofu.clairvoyance.web.entity.SheetField;
import cn.shuangbofu.clairvoyance.web.enums.ColumnType;
import cn.shuangbofu.clairvoyance.web.enums.FieldType;
import com.fasterxml.jackson.databind.JsonNode;
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

    private FieldType fieldType;

    private ColumnType type;

    private String remarks;

    private Integer seqNo;

    private String linkUrl;

    private String config;

    private String formula;

    private List<Long> fieldList;

    public static List<FieldSimpleVO> toVOs(List<SheetField> sheetFields) {
        return sheetFields.stream().map(FieldSimpleVO::toVO).collect(Collectors.toList());
    }

    public static FieldSimpleVO toVO(SheetField sheetField) {
        FieldSimpleVO fieldSimpleVO = new FieldSimpleVO().setId(sheetField.getId())
                .setName(sheetField.getName())
                .setTitle(sheetField.getTitle())
                .setType(sheetField.getColumnType())
                .setRemarks(sheetField.getRemarks())
                .setSeqNo(sheetField.getSeqNo())
                .setLinkUrl(sheetField.getLinkUrl())
                .setFieldType(sheetField.getFieldType())
                .setConfig(sheetField.getConfig());

        if (FieldType.computed.equals(sheetField.getFieldType())) {
            JsonNode node = JSON.jsonString2JsonNode(sheetField.getConfig());
            fieldSimpleVO.setFormula(node.get("formula").asText());
            fieldSimpleVO.setFieldList(JSON.parseArray(node.get("fieldList").asText(), Long.class));
        }

        return fieldSimpleVO;
    }

    public SheetField toModel() {
        return new SheetField().setId(id).setTitle(title).setColumnType(type).setRemarks(remarks).setLinkUrl(linkUrl);
    }
}
