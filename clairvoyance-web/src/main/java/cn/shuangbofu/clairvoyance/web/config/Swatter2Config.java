package cn.shuangbofu.clairvoyance.web.config;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by shuangbofu on 2020/7/30 下午11:14
 */
@Configuration
@EnableSwagger2
public class Swatter2Config {

    @Bean("dashboardApis")
    public Docket dashboardApis() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("test")
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .enable(true);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("clairvoyance接口测试")
                .description("clairvoyance数据可视化")
                .termsOfServiceUrl("https://localhost:8891/")
                .version("1.0.0")
                .build();
    }

}
