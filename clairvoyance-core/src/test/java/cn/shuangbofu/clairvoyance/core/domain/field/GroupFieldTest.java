package cn.shuangbofu.clairvoyance.core.domain.field;

import cn.shuangbofu.clairvoyance.core.db.Chart;
import cn.shuangbofu.clairvoyance.core.enums.FieldType;
import io.github.biezhi.anima.Anima;
import junit.framework.TestCase;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/10 12:33
 */
public class GroupFieldTest extends TestCase {

    public void test() {

        Anima.open("jdbc:mysql://127.0.0.1:3306/clairvoyance?useSSL=false&characterEncoding=UTF-8", "root", "root");

        String config = "{\"refId\"}";
        cn.shuangbofu.clairvoyance.core.db.Field field = new cn.shuangbofu.clairvoyance.core.db.Field().setConfig("{\n" +
                "\t\"refId\": 4,\n" +
                "    \"mode\": \"condition\",\n" +
                "    \"groups\": [{\n" +
                "  \t\t\"name\": \"是\",\n" +
                "        \"expression\": \"[节点引用ID]=0\"\n" +
                "    },{\n" +
                "  \t\t\"name\": \"否\",\n" +
                "        \"expression\": \"[节点引用ID]!=0\"\n" +
                "    }]\n" +
                "}").setName("是否是文件夹").setTitle("是否是文件夹").setWorkSheetId(1L).setFieldType(FieldType.group);
//        Field f = Field.fromDb(field);
//        GroupField groupField = (GroupField) f;

//        String name = groupField.getName();
//        System.out.println(name);
//        System.out.println(groupField.getRange());
    }


    public void test2() {
        Anima.open("jdbc:mysql://127.0.0.1:3306/clairvoyance?useSSL=false&characterEncoding=UTF-8", "root", "root");

        List<Chart> charts = Chart.from().select("id, sql_config").all();
        for (Chart chart : charts) {
            System.out.println(chart.getId() + " ====> " + chart.getSqlConfig());
        }
    }

}
