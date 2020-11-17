package cn.shuangbofu.clairvoyance.web.config;

import cn.shuangbofu.clairvoyance.web.dao.BaseDao;
import cn.shuangbofu.clairvoyance.web.entity.Model;
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

    @Value("${runtime.env}")
    public void setEnv(String value) {
        Model.ENV = value;
        BaseDao.ENV = value;
    }

    @Bean
    public Anima anima() {
        Anima anima = Anima.open(jdbcUrl, username, password);
        FixData.fix();
        return anima;
    }
}
