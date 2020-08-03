package cn.shuangbofu.clairvoyance.core.domain.worksheet;

import cn.shuangbofu.clairvoyance.core.domain.Pair;
import cn.shuangbofu.clairvoyance.core.enums.FieldType;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by shuangbofu on 2020/7/30 下午9:21
 * <p>
 * TODO 字段要不要单独搞个表？
 */
@Data
@ApiModel(value = "字段", description = "根据原字段名修改新字段名、类型、注释")
public class Field implements Comparable<Field> {

    @ApiModelProperty("原字段名")
    private String name;

    @ApiModelProperty("新字段名")
    private String title;

    @ApiModelProperty("字段类型")
    private FieldType type;

    @ApiModelProperty("字段注释")
    private String remarks;

    private int seqNo;

    @ApiModelProperty("链接")
    private String linkUrl;

    public static List<Field> strToFields(String fieldInfos) {
        return JSON.parseArray(fieldInfos, Field.class);
    }

    public static Pair<Boolean, String> updateOne(String fieldInfos, Field field) {
        List<Field> fields = strToFields(fieldInfos);
        boolean updated = false;
        for (Field f : fields) {
            if (f.getName().equals(field.getName())) {
                if (field.getType() != null) {
                    f.setType(field.getType());
                }

                if (field.getRemarks() != null) {
                    f.setRemarks(field.getRemarks());
                }

                if (field.getTitle() != null) {
                    f.setTitle(field.getTitle());
                }
                updated = true;
            }
        }
        String newVal = JSON.toJSONString(fields);
        return new Pair<>(updated, newVal);
    }

    public static void main(String[] args) {
        String str = "[\n" +
                "      {\n" +
                "        \"remarks\": \"\",\n" +
                "        \"name\": \"id\",\n" +
                "        \"title\": \"主键\",\n" +
                "        \"field_id\": \"fkd761ba66\",\n" +
                "        \"real_type\": null,\n" +
                "        \"seqNo\": 0,\n" +
                "        \"uniq_index\": 0,\n" +
                "        \"type\": \"value\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"remarks\": \"\",\n" +
                "        \"name\": \"task_id\",\n" +
                "        \"title\": \"任务ID\",\n" +
                "        \"field_id\": \"fk9bc0fa59\",\n" +
                "        \"real_type\": null,\n" +
                "        \"seqNo\": 1,\n" +
                "        \"uniq_index\": 0,\n" +
                "        \"type\": \"value\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"remarks\": \"\",\n" +
                "        \"name\": \"version\",\n" +
                "        \"title\": \"任务版本号\",\n" +
                "        \"field_id\": \"fkc1a5d6db\",\n" +
                "        \"real_type\": null,\n" +
                "        \"seqNo\": 2,\n" +
                "        \"uniq_index\": 0,\n" +
                "        \"type\": \"value\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"remarks\": \"\",\n" +
                "        \"name\": \"env\",\n" +
                "        \"title\": \"环境\",\n" +
                "        \"field_id\": \"fkde6d9e7b\",\n" +
                "        \"real_type\": null,\n" +
                "        \"seqNo\": 3,\n" +
                "        \"uniq_index\": 0,\n" +
                "        \"type\": \"text\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"remarks\": \"\",\n" +
                "        \"name\": \"sql_text\",\n" +
                "        \"title\": \"执行SQL\",\n" +
                "        \"field_id\": \"fkb3b1cccb\",\n" +
                "        \"real_type\": null,\n" +
                "        \"seqNo\": 4,\n" +
                "        \"uniq_index\": 0,\n" +
                "        \"type\": \"text\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"remarks\": \"\",\n" +
                "        \"name\": \"task_params\",\n" +
                "        \"title\": \"环境参数\",\n" +
                "        \"field_id\": \"fk5013bc97\",\n" +
                "        \"real_type\": null,\n" +
                "        \"seqNo\": 5,\n" +
                "        \"uniq_index\": 0,\n" +
                "        \"type\": \"text\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"remarks\": \"\",\n" +
                "        \"name\": \"source_params\",\n" +
                "        \"title\": \"源表配置\",\n" +
                "        \"field_id\": \"fk7c7ff0d0\",\n" +
                "        \"real_type\": null,\n" +
                "        \"seqNo\": 6,\n" +
                "        \"uniq_index\": 0,\n" +
                "        \"type\": \"text\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"remarks\": \"\",\n" +
                "        \"name\": \"side_params\",\n" +
                "        \"title\": \"维表配置\",\n" +
                "        \"field_id\": \"fk4e864dc4\",\n" +
                "        \"real_type\": null,\n" +
                "        \"seqNo\": 7,\n" +
                "        \"uniq_index\": 0,\n" +
                "        \"type\": \"text\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"remarks\": \"\",\n" +
                "        \"name\": \"sink_params\",\n" +
                "        \"title\": \"结果表配置\",\n" +
                "        \"field_id\": \"fkd7eabb30\",\n" +
                "        \"real_type\": null,\n" +
                "        \"seqNo\": 8,\n" +
                "        \"uniq_index\": 0,\n" +
                "        \"type\": \"text\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"remarks\": \"\",\n" +
                "        \"name\": \"resource_config\",\n" +
                "        \"title\": \"资源配置\",\n" +
                "        \"field_id\": \"fk83fb32d7\",\n" +
                "        \"real_type\": null,\n" +
                "        \"seqNo\": 9,\n" +
                "        \"uniq_index\": 0,\n" +
                "        \"type\": \"text\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"remarks\": \"\",\n" +
                "        \"name\": \"main_class\",\n" +
                "        \"title\": \"main_class\",\n" +
                "        \"field_id\": \"fk50b32da6\",\n" +
                "        \"real_type\": null,\n" +
                "        \"seqNo\": 10,\n" +
                "        \"uniq_index\": 0,\n" +
                "        \"type\": \"text\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"remarks\": \"\",\n" +
                "        \"name\": \"exe_args\",\n" +
                "        \"title\": \"其他运行参数\",\n" +
                "        \"field_id\": \"fk1b154fad\",\n" +
                "        \"real_type\": null,\n" +
                "        \"seqNo\": 11,\n" +
                "        \"uniq_index\": 0,\n" +
                "        \"type\": \"text\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"remarks\": \"\",\n" +
                "        \"name\": \"create_user_id\",\n" +
                "        \"title\": \"创建人ID\",\n" +
                "        \"field_id\": \"fk1dad476a\",\n" +
                "        \"real_type\": null,\n" +
                "        \"seqNo\": 12,\n" +
                "        \"uniq_index\": 0,\n" +
                "        \"type\": \"value\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"remarks\": \"\",\n" +
                "        \"name\": \"modify_user_id\",\n" +
                "        \"title\": \"修改人ID\",\n" +
                "        \"field_id\": \"fkaae5b3f8\",\n" +
                "        \"real_type\": null,\n" +
                "        \"seqNo\": 13,\n" +
                "        \"uniq_index\": 0,\n" +
                "        \"type\": \"value\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"remarks\": \"\",\n" +
                "        \"name\": \"gmt_create\",\n" +
                "        \"title\": \"gmt_create\",\n" +
                "        \"field_id\": \"fk32ff3ec6\",\n" +
                "        \"real_type\": null,\n" +
                "        \"seqNo\": 14,\n" +
                "        \"uniq_index\": 0,\n" +
                "        \"type\": \"value\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"remarks\": \"\",\n" +
                "        \"name\": \"gmt_modified\",\n" +
                "        \"title\": \"gmt_modified\",\n" +
                "        \"field_id\": \"fk355114db\",\n" +
                "        \"real_type\": null,\n" +
                "        \"seqNo\": 15,\n" +
                "        \"uniq_index\": 0,\n" +
                "        \"type\": \"value\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"remarks\": \"\",\n" +
                "        \"name\": \"deleted\",\n" +
                "        \"title\": \"是否删除\",\n" +
                "        \"field_id\": \"fk47c5f970\",\n" +
                "        \"real_type\": null,\n" +
                "        \"seqNo\": 16,\n" +
                "        \"uniq_index\": 0,\n" +
                "        \"type\": \"value\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"remarks\": \"\",\n" +
                "        \"name\": \"source_text\",\n" +
                "        \"title\": \"源表SQL\",\n" +
                "        \"field_id\": \"fkade643dd\",\n" +
                "        \"real_type\": null,\n" +
                "        \"seqNo\": 17,\n" +
                "        \"uniq_index\": 0,\n" +
                "        \"type\": \"text\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"remarks\": \"\",\n" +
                "        \"name\": \"side_text\",\n" +
                "        \"title\": \"维表SQL\",\n" +
                "        \"field_id\": \"fk754169ae\",\n" +
                "        \"real_type\": null,\n" +
                "        \"seqNo\": 18,\n" +
                "        \"uniq_index\": 0,\n" +
                "        \"type\": 0\n" +
                "      },\n" +
                "      {\n" +
                "        \"remarks\": \"\",\n" +
                "        \"name\": \"sink_text\",\n" +
                "        \"title\": \"结果表SQL\",\n" +
                "        \"field_id\": \"fka3c62317\",\n" +
                "        \"real_type\": null,\n" +
                "        \"seqNo\": 19,\n" +
                "        \"uniq_index\": 0,\n" +
                "        \"type\": \"text\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"remarks\": \"\",\n" +
                "        \"name\": \"user_key_value\",\n" +
                "        \"title\": \"用户自定义变量\",\n" +
                "        \"field_id\": \"fk6906fdf7\",\n" +
                "        \"real_type\": null,\n" +
                "        \"seqNo\": 20,\n" +
                "        \"uniq_index\": 0,\n" +
                "        \"type\": \"text\"\n" +
                "      }\n" +
                "    ]";
        String s = JSON.toJSONString(Field.strToFields(str));
        System.out.println(s);
    }

    @Override
    public int compareTo(Field o) {
        if (o == null) {
            return 1;
        }

        if (getSeqNo() == o.getSeqNo()) {
            return 0;
        } else {
            return getSeqNo() - o.getSeqNo() > 0 ? 1 : -1;
        }
    }
}
