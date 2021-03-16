package com.rebwon.toby.learning.spring.web;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManagerFactoryBean;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Configuration
public class TestAppConfig {

  @Bean
  public ContentNegotiatingViewResolver contentNegotiatingViewResolver() {
    ContentNegotiatingViewResolver contentNegotiatingViewResolver = new ContentNegotiatingViewResolver();
    contentNegotiatingViewResolver.setDefaultViews(List.of(new MappingJackson2JsonView()));
    ContentNegotiationManagerFactoryBean contentNegotiationManagerFactoryBean = new ContentNegotiationManagerFactoryBean();
    contentNegotiationManagerFactoryBean.addMediaType("json", MediaType.APPLICATION_JSON);
    contentNegotiatingViewResolver.setContentNegotiationManager(contentNegotiationManagerFactoryBean.build());
    return contentNegotiatingViewResolver;
  }
}
