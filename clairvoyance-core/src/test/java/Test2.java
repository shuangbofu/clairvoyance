import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/8/5 11:24
 */
public class Test2 {
    public static void main(String[] args) {
        String jsonString = "{\n" +
                "  \"as\":[{\n" +
                "    \"key\":\"b\",\n" +
                "    \"name:\":\"1\",\n" +
                "    \"value\":\"1\"\n" +
                "  },{\n" +
                "    \"key\":\"c\",\n" +
                "    \"name\":\"2\",\n" +
                "    \"value\":\"2\",\n" +
                "    \"ddd\":\"2\"\n" +
                "  },{\n" +
                "    \"key\":\"a\"\n" +
                "  }]\n" +
                "}";

        J j = new J();
        B b = new B("b", "b");
        b.setKey("b");

        C c = new C("c");
        c.setName("c");
        c.setKey("c");
        c.setValue("c");

        j.setAs(Lists.newArrayList(b, c));

        String s = JSON.toJSONString(j);
        System.out.println(s);

        J parseJ = JSON.parseObject(s, J.class);

//        for (A a : parseJ.as) {
//            if (a instanceof B) {
//                B bb = (B) a;
//                System.out.println(bb.get());
//            } else if (a instanceof C) {
//                C cc = (C) a;
//                System.out.println(cc.get());
//            }
//        }
        System.out.println(parseJ);
    }

    @Data
    @NoArgsConstructor
    @JSONType(seeAlso = {
            B.class, C.class
    })
    static class A {
        String key;

        public String get() {
            return key;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JSONType(typeName = "b")
    static class B extends A {
        String name;
        String value;

        @Override
        public String get() {
            return super.get() + ":" + name + ":" + value;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JSONType(typeName = "c")
    static class C extends B {
        String ddd;

        @Override
        public String get() {
            return super.get() + ":" + ddd;
        }
    }

    @Data
    static class J {
        @JSONField(parseFeatures = {Feature.SupportAutoType})
        List<A> as = Lists.newArrayList();

        @Override
        public String toString() {
            return as.stream().map(A::get).collect(Collectors.joining(","));
        }
    }

    public static class ADeserializer implements ObjectDeserializer {

        @Override
        public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object o) {
            System.out.println(o);
            return null;
        }

        @Override
        public int getFastMatchToken() {
            return 0;
        }
    }
}
