package com.rebwon.toby.learning.spring.bean;

import javax.annotation.PreDestroy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class DestroyBeanTest {

  @Test
  void destroyBean() {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(
        TestConfig.class);
  }

  @Configuration
  static class TestConfig {

    @Bean(destroyMethod = "destroy")
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
    public void destroy() {
      System.out.println("Destroy BeanC");
    }
  }

  static class BeanB {
    @PreDestroy
    public void init() {
      System.out.println("Destroy BeanB");
    }
  }

  static class BeanA implements DisposableBean {

    @Override
    public void destroy() throws Exception {
      System.out.println("Destroy BeanA");
    }
  }
}
