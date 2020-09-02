package cn.shuangbofu.clairvoyance.core.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.List;

/**
 * Created by shuangbofu on 2020/8/8 下午11:20
 */
public class JSON {

    public static final ObjectMapper MAPPER = new ObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
//            .enable(SerializationFeature.INDENT_OUTPUT)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);

    public static <T> T parseObject(String json, Class<T> tClass) {
        try {
            return MAPPER.readValue(json, tClass);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("parse json error");
        }
    }

    public static <T> List<T> parseArray(String json, Class<T> tClass) {
        try {
            JavaType javaType = MAPPER.getTypeFactory()
                    .constructCollectionType(List.class, tClass);
            return MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("parse json 2 array error");
        }
    }

    public static String toJSONString(Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("bean to json error");
        }
    }
}
