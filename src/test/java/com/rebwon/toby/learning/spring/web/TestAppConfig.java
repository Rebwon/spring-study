package com.rebwon.toby.learning.spring.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.accept.ContentNegotiationManagerFactoryBean;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Configuration
public class TestAppConfig {

  @Bean
  @Order(value = 1)
  public ContentNegotiatingViewResolver contentNegotiatingViewResolver() {
    Map<String, MediaType> map = new HashMap<>();
    map.put("atom", MediaType.APPLICATION_ATOM_XML);
    map.put("json", MediaType.APPLICATION_JSON);
    map.put("html", MediaType.TEXT_HTML);
    ContentNegotiationManagerFactoryBean contentNegotiationManagerFactoryBean = new ContentNegotiationManagerFactoryBean();
    contentNegotiationManagerFactoryBean.addMediaTypes(map);
    ContentNegotiationManager negotiationManager = contentNegotiationManagerFactoryBean.build();
    ContentNegotiatingViewResolver contentNegotiatingViewResolver = new ContentNegotiatingViewResolver();
    contentNegotiatingViewResolver.setDefaultViews(List.of(mappingJackson2JsonView()));
    contentNegotiatingViewResolver.setViewResolvers(List.of(internalResourceViewResolver(), beanNameViewResolver()));
    contentNegotiatingViewResolver.setContentNegotiationManager(negotiationManager);
    return contentNegotiatingViewResolver;
  }

  @Bean
  public MappingJackson2JsonView mappingJackson2JsonView() {
    return new MappingJackson2JsonView();
  }

  @Bean
  public InternalResourceViewResolver internalResourceViewResolver() {
    InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
    internalResourceViewResolver.setSuffix(".html");
    return internalResourceViewResolver;
  }

  @Bean
  public BeanNameViewResolver beanNameViewResolver() {
    return new BeanNameViewResolver();
  }
}
