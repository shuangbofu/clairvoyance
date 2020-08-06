import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.JavaBeanInfo;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
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
//
//        SerializerFeature[] features = new SerializerFeature[]{
//                SerializerFeature.WriteClassName,
//                //SerializerFeature.SkipTransientField,
//                //SerializerFeature.DisableCircularReferenceDetect
//        };
//        J j = JSON.parseObject(jsonString, J.class, new TParserConfig(), JSON.DEFAULT_PARSER_FEATURE, new Feature[0]);
//
//        for (A a : j.as) {
//            if (a instanceof B) {
//                System.out.println(a.get());
//            } else if (a instanceof C) {
//                System.out.println(a.get());
//            } else {
//                System.out.println(a.get());
//            }
//        }
//        System.out.println(j.as);
//        System.out.println(j.toString());

        J j = JSON.parseObject(jsonString, J.class);

        for (A a : j.as) {
            System.out.println(a.get());
        }
    }

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface JsonMaps {
        JsonMap[] values();
    }


    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface JsonMap {
        String key();

        Class<?> value();
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonMaps(values = {
            @JsonMap(key = "a", value = A.class),
            @JsonMap(key = "b", value = B.class),
            @JsonMap(key = "c", value = C.class)
    })
    static abstract class A {
        String key;

        public String get() {
            return key;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class B extends A {
        String name;
        String value;

        @Override
        public String get() {
            return super.get() + ":" + name + ":" + value;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class C extends B {
        String ddd;

        @Override
        public String get() {
            return super.get() + ":" + ddd;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class J {
        List<A> as = Lists.newArrayList();

        @Override
        public String toString() {
            return as.stream().map(A::get).collect(Collectors.joining(","));
        }
    }

    public static class PolymorphismDeserializer extends JavaBeanDeserializer {

        private static final Logger logger = LoggerFactory.getLogger(PolymorphismDeserializer.class);

        private static final String TARGET_FIELD = "type";
        private static final String TYPE_NAME_PREFIX = "class ";
        private static final String JAVA_BEAN_DESERIALIZER_BEANINFO_FIELD = "beanInfo";

        public PolymorphismDeserializer(ParserConfig config, Class<?> clazz, Type type) {
            super(config, clazz, type);
        }

        @Override
        public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
            String jsonstr = parser.getInput();
            Optional<String> targetKey = Optional.ofNullable(parseTarget(jsonstr));
            if (targetKey.isPresent()) {
                logger.debug("目标类的key:" + targetKey.get());
                String className = type.toString();
                if (className.startsWith(TYPE_NAME_PREFIX)) {
                    className = className.substring(TYPE_NAME_PREFIX.length());
                }
                try {
                    //解析实际类
                    Class<?> clazz = Class.forName(className);
                    if (clazz.isAnnotationPresent(JsonMaps.class)) {
                        JsonMaps jsonMaps = clazz.getAnnotation(JsonMaps.class);
                        JsonMap[] js = jsonMaps.values();
                        boolean flag = false;
                        for (JsonMap j : js) {
                            if (targetKey.get().equals(j.key())) {
                                Class<?> target = j.value();
                                logger.debug("目标类:" + target.getName());
                                logger.debug("modifier:" + Modifier.toString(target.getModifiers()));
                                type = (Type) target;

                                //由于config中getDeserializer方法中，调用JavaBeanDeserializer时已经把抽象类或接口的信息计算了，这里需要替换成实现类的信息

                                JavaBeanInfo beanInfo = JavaBeanInfo.build(clazz, type, PropertyNamingStrategy.NoChange);
                                //由于JavaBeanDeserializer中的beanInfo字段为private并且没有setter，只能通过反射设置
                                Field beanInfoField = JavaBeanDeserializer.class.getDeclaredField(JAVA_BEAN_DESERIALIZER_BEANINFO_FIELD);
                                beanInfoField.setAccessible(true);
                                beanInfoField.set(this, beanInfo);
                                flag = true;
                                break;
                            }
                        }
                        if (!flag) {
                            logger.error("没有找到指定的类");
                            throw new RuntimeException(targetKey.get());
                        }
                    } else {
                        logger.error("没有指定JsonMaps");
                        throw new RuntimeException(type.getTypeName());
                    }
                } catch (ClassNotFoundException | NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                logger.error("没有指定key");
                throw new RuntimeException(jsonstr);
            }
            return super.deserialze(parser, type, fieldName);
        }

        private String parseTarget(String jsonstr) {
            String[] ts = jsonstr.split("\"" + TARGET_FIELD + "\":");
            return Optional.ofNullable(ts[1])
                    .orElse("")
                    .split("\"|\"")[1];
        }

    }

    static class TParserConfig extends ParserConfig {

        @Override
        public ObjectDeserializer getDeserializer(Class<?> clazz, Type type) {
            //实现了空的接口，表明需要多态解析
            if (clazz == A.class) {
                //返回我们自定义的多态反序列化工具，直接通过构造器，没有用asm
                return new PolymorphismDeserializer(this, clazz, type);
            }
            return super.getDeserializer(clazz, type);

        }
    }
}
