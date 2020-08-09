package cn.shuangbofu.clairvoyance.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by shuangbofu on 2020/7/30 下午8:16
 */
@Component
@ImportResource(locations = {"classpath:spring.xml"})
@SpringBootApplication(scanBasePackages = {"cn.shuangbofu.clairvoyance.**"})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Controller
    static class IndexController {
        @RequestMapping("/")
        public String index() {
            return "index.html";
        }
    }
}
