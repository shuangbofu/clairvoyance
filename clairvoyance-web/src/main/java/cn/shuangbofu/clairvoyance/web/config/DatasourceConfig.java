package cn.shuangbofu.clairvoyance.web.config;

import io.github.biezhi.anima.Anima;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by shuangbofu on 2020/7/30 下午8:26
 */
@Configuration
public class DatasourceConfig {

    @Value("${mysql.url}")
    private String jdbcUrl;

    @Value("${mysql.username}")
    private String username;

    @Value("${mysql.password}")
    private String password;

    @Bean
    public Anima anima() {
        return Anima.open(jdbcUrl, username, password);
    }
}
