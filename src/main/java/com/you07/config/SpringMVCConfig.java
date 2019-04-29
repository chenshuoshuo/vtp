package com.you07.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class SpringMVCConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private MyWebFilter myWebFilter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myWebFilter)
                .addPathPatterns("/**")
                .excludePathPatterns("/**/*.css",
                        "/**/*.js",
                        "/**/*.png",
                        "/**/*.jpg",
                        "/**/*.jpeg",
                        "/*.html",
                        "/**/*.html",
                        "/swagger-resources/**");
    }
}
