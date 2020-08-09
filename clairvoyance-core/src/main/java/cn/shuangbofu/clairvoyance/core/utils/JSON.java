package cn.shuangbofu.clairvoyance.core.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by shuangbofu on 2020/8/8 下午11:20
 */
public class JSON {
    public static <T> T parseObject(String json, Class<T> tClass) {
        try {
            return new ObjectMapper()
                    .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                    .configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true).readValue(json, tClass);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("parse json error");
        }
    }

    public static String toJSONString(Object object) {
        try {
            return new ObjectMapper()
                    .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                    .configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true).writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("bean to json error");
        }
    }

    public static JSONObject parseObject(String json) {
        return parseObject(json, JSONObject.class);
    }
}
