package com.rebwon.toby.learning.spring.bean;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

public class ConfigurationBeanNameTest {

  @Test
  public void registerOuterConfig() {
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
    ctx.register(A.class);
    ctx.refresh();
    assertThat(ctx.containsBean("outer")).isTrue();
    assertThat(ctx.containsBean("imported")).isTrue();
    assertThat(ctx.containsBean("nested")).isTrue();
    assertThat(ctx.containsBean("nestedBean")).isTrue();
  }

  @Test
  public void registerNestedConfig() {
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
    ctx.register(A.B.class);
    ctx.refresh();
    assertThat(ctx.containsBean("outer")).isFalse();
    assertThat(ctx.containsBean("imported")).isFalse();
    assertThat(ctx.containsBean("nested")).isTrue();
    assertThat(ctx.containsBean("nestedBean")).isTrue();
  }

  @Test
  public void registerOuterConfig_withBeanNameGenerator() {
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
    ctx.setBeanNameGenerator(new AnnotationBeanNameGenerator() {
      @Override
      public String generateBeanName(
          BeanDefinition definition, BeanDefinitionRegistry registry) {
        return "custom-" + super.generateBeanName(definition, registry);
      }
    });
    ctx.register(A.class);
    ctx.refresh();
    assertThat(ctx.containsBean("custom-outer")).isTrue();
    assertThat(ctx.containsBean("custom-imported")).isTrue();
    assertThat(ctx.containsBean("custom-nested")).isTrue();
    assertThat(ctx.containsBean("nestedBean")).isTrue();
  }

  @Configuration("outer")
  @Import(C.class)
  static class A {
    @Component("nested")
    static class B {
      @Bean
      public String nestedBean() { return ""; }
    }
  }

  @Configuration("imported")
  static class C {
    @Bean public String s() { return "s"; }
  }
}
