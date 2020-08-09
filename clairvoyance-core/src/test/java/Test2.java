//import cn.shuangbofu.clairvoyance.core.utils.JSON;
//import com.alibaba.fastjson.annotation.JSONField;
//import com.alibaba.fastjson.annotation.JSONType;
//import com.alibaba.fastjson.serializer.SerializerFeature;
//import com.google.common.collect.Lists;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import lombok.NoArgsConstructor;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * Created by shuangbofu on 2020/8/5 11:24
// */
//public class Test2 {
//    public static void main(String[] args) {
//        String jsonString = "{\n" +
//                "  \"as\":[{\n" +
//                "    \"key\":\"bbb\",\n" +
//                "    \"name:\":\"1\",\n" +
//                "    \"value\":\"1\"\n" +
//                "  },{\n" +
//                "    \"key\":\"d\",\n" +
//                "    \"name\":\"2\",\n" +
//                "    \"value\":\"2\",\n" +
//                "    \"ddd\":\"2\"\n" +
//                "  }]\n" +
//                "}";
//        J j = new J();
//        B b = new B();
//        b.setKey("d");
//        b.setName("1");
//        b.setValue("1");
////        C c = new C();
////        c.setDdd("2");
////        c.setName("2");
////        c.setKey("c");
////        c.setValue("2");
//        j.setAs(Lists.newArrayList(b
////                , c
//        ));
//        String s = JSON.toJSONString(j, SerializerFeature.PrettyFormat);
//
//        J parseJ = JSON.parseObject(jsonString, J.class);
//        System.out.println(s);
//        System.out.println(parseJ.getAs().stream().map(A::get).collect(Collectors.joining(",")));
//
//
//        System.out.println("------");
//        System.out.println(jsonString);
//        J j1 = JSON.parseObject(s, J.class);
//        System.out.println(j1.getAs().stream().map(A::get).collect(Collectors.joining(",")));
//
//
//        String cString = "{\n" +
//                "    \"key\":\"c\",\n" +
//                "    \"name\":\"2\",\n" +
//                "    \"value\":\"2\",\n" +
//                "    \"ddd\":\"2\"\n" +
//                "  }";
//
////        C c1 = JSON.parseObject(cString, C.class);
////        System.out.println(c1);
//    }
//
//    @NoArgsConstructor
//    @JSONType(seeAlso = {
//            B.class,
//            D.class
////            C.class
//    }, typeKey = "key")
//    static class A {
//        String key;
//
//        public String get() {
//            return key;
//        }
//
//        public String getKey() {
//            return key;
//        }
//
//        public void setKey(String key) {
//            this.key = key;
//        }
//    }
//
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @JSONType(typeName = "bbb")
//    static class B extends A {
//        String name;
//        String value;
//
//        @Override
//        public String get() {
//            return super.get() + ":" + name + ":" + value;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public String getValue() {
//            return value;
//        }
//
//        public void setValue(String value) {
//            this.value = value;
//        }
//    }
//
//    @EqualsAndHashCode(callSuper = true)
//    @JSONType(typeName = "d")
//    static class D extends A {
//
//    }
//
////    @Data
////    @NoArgsConstructor
////    @AllArgsConstructor
////    @JSONType(typeName = "c")
////    static class C extends B {
////        String ddd;
////
////        @Override
////        public String get() {
////            return super.get() + ":" + ddd;
////        }
////    }
//
//    @Data
//    static class J {
//        @JSONField
////        @JSONField(deserializeUsing = AdderSerializer.class)
//                List<A> as = Lists.newArrayList();
//
//        @Override
//        public String toString() {
//            return as.stream().map(A::get).collect(Collectors.joining(","));
//        }
//    }
//}
