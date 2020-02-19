package com.question.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.question.admin.config.props.DbBackupProperties;
import com.question.admin.config.shiro.security.JwtProperties;
import com.question.admin.config.wx.SystemConfig;
import com.question.admin.support.XssSqlStringJsonSerializer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@SpringBootApplication
//@ImportResource(locations={"classpath:spring/spring-config-*.xml"})
@MapperScan("com.question.admin.mapper")
@EnableConfigurationProperties({JwtProperties.class, DbBackupProperties.class, SystemConfig.class})
@EnableAutoConfiguration
public class AdminApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }
    @Bean
    @Primary
    public ObjectMapper xssObjectMapper(Jackson2ObjectMapperBuilder builder) {
        // 解析器
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        // 注册XSS SQL 解析器
        SimpleModule xssModule = new SimpleModule("XssStringJsonSerializer");
        xssModule.addSerializer(new XssSqlStringJsonSerializer());
        objectMapper.registerModule(xssModule);
        return objectMapper;
    }
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

}
