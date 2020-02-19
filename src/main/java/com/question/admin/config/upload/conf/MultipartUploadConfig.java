package com.question.admin.config.upload.conf;

import com.question.admin.config.upload.aop.MultipartHandlerInterceptor;
import com.question.admin.config.upload.support.DefaultFileArchiveStrategy;
import com.question.admin.config.upload.support.DefaultFileResolver;
import com.question.admin.config.upload.support.FileResolver;
import com.question.admin.config.upload.support.FileArchiveStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zvc
 */
@Configuration
public class MultipartUploadConfig implements WebMvcConfigurer { //WebMvcConfigurationSupport

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/swagger/**")
                .addResourceLocations("classpath:/META-INF/resources/swagger*");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MultipartHandlerInterceptor()).addPathPatterns("/**");
//        super.addInterceptors(registry);
    }

//    @Bean
//    public MultipartAspect multipartAspect() {
//        return new MultipartAspect();
//    }


//    @Bean
//    @ConditionalOnMissingBean(StorageService.class)
//    public StorageService defaultStorageService() {
//        return new StorageServiceImpl();
//    }

    @Bean
    @ConditionalOnMissingBean(FileArchiveStrategy.class)
    public FileArchiveStrategy fileArchiveStrategy() {
        return new DefaultFileArchiveStrategy();
    }

    @Bean
    @ConditionalOnMissingBean(FileResolver.class)
    public FileResolver defaultFileResolver() {
        return new DefaultFileResolver();
    }

}
