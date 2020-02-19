package com.question.admin.config;

import com.question.admin.constant.SecurityConsts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@EnableSwagger2
@Configuration
public class SwaggerCofing{
    @Bean
    public Docket apiConfig(){
        ParameterBuilder builder = new ParameterBuilder();
            builder.parameterType("header").name(SecurityConsts.REQUEST_AUTH_HEADER)
                .description("restful方式的header参数")
                .required(false)
                .modelRef(new ModelRef("string")); // 在swagger里显示header
        List<Parameter> pars = new ArrayList<Parameter>();
        pars.add(builder.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.question.admin.web")).paths(PathSelectors.any()).build()//过滤的接口
                .groupName("Question-ADMIN 后端接口文档") //定义分组
                .apiInfo(apiInfo()) //展示在文档页面信息内容
                .useDefaultResponseMessages(false).globalOperationParameters(pars);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Question-ADMIN API")
                .description("Question-ADMIN's REST API")//详细描述
                .version("1.0")
                .contact(new Contact("zvc", "", ""))//作者
//                .license("The Apache License, Version 2.0")//许可证信息
//                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")//许可证地址
                .build();
    }


}