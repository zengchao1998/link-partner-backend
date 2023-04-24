package com.wut.self.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @author zeng
 * Swagger 配置类
 */
@Configuration
@EnableSwagger2WebMvc
@Profile({"dev", "test"})
public class SwaggerConfig {

    @Bean
    public Docket webApiConfig(){

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(webApiInfo())  // api 文档的信息
                .select()  // 选择 api
                .apis(RequestHandlerSelectors.basePackage("com.wut.self.controller")) // api 所在包的位置
                .paths(PathSelectors.any())
                .build();

    }

    private ApiInfo webApiInfo(){
        return new ApiInfoBuilder()
                .title("用户中心")
                .description("用户中心接口文档")
                .termsOfServiceUrl("https://github.com/zengchao1998")
                .version("1.0")
                .contact(new Contact("java", "https://github.com/zengchao1998", "zc981211@gmail.com"))
                .build();
    }
}
