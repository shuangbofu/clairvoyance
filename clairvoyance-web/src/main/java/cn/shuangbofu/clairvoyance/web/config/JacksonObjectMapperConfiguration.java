package cn.shuangbofu.clairvoyance.web.config;

import cn.shuangbofu.clairvoyance.core.utils.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * Created by shuangbofu on 2020/9/18 上午12:31
 */
@ConditionalOnClass({Jackson2ObjectMapperBuilder.class})
@Configuration
public class JacksonObjectMapperConfiguration {
    @Bean
    @Primary
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        return JSON.setupMapper(builder.createXmlMapper(false).build());
    }
}
