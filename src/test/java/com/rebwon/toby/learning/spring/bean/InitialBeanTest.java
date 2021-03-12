package com.rebwon.toby.learning.spring.bean;

import javax.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class InitialBeanTest {

  @Test
  void initializingBean() {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(
        TestConfig.class);
  }

  @Configuration
  static class TestConfig {

    @Bean(initMethod = "init")
    public BeanC beanC() {
      return new BeanC();
    }

    @Bean
    public BeanB beanB() {
      return new BeanB();
    }

    @Bean
    public BeanA beanA() {
      return new BeanA();
    }
  }

  static class BeanC {
    public void init() {
      System.out.println("Init BeanC");
    }
  }

  static class BeanB {
    @PostConstruct
    public void init() {
      System.out.println("Init BeanB");
    }
  }

  static class BeanA implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
      System.out.println("Init BeanA");
    }
  }
}
