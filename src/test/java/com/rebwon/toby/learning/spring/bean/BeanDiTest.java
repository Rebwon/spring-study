package com.rebwon.toby.learning.spring.bean;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

public class BeanDiTest {

  @Test
  void diTest() {
    String something = Something.filterBindMarker("something");
    System.out.println(something);

  }

  static class Something {

    public static String filterBindMarker(CharSequence input) {
      StringBuilder builder = new StringBuilder(input.length());
      for (int i = 0; i < input.length(); i++) {
        char ch = input.charAt(i);
        // ascii letter or digit
        if (Character.isLetterOrDigit(ch) && ch < 127) {
          builder.append(ch);
        }
      }
      if (builder.length() == 0) {
        return "";
      }
      return "_" + builder;
    }
  }

  static class FooService {
    private Formatter fooFormatter;

    @Autowired
    public void setFormatter(Formatter fooFormatter) {
      this.fooFormatter = fooFormatter;
    }
  }

  @Configuration
  static class TestConfig {
    @Bean
    public FooService fooService(
        @Qualifier("barFormatter") Formatter formatter) {
      FooService fooService = new FooService();
      fooService.setFormatter(formatter);
      return fooService;
    }

    @Bean
    public Formatter fooFormatter() {
      return new FooFormatter();
    }

    @Bean
    public Formatter barFormatter() {
      return new BarFormatter();
    }
  }

  interface Formatter {
    String format();
  }

  public static class FooFormatter implements Formatter {
    public String format() {
      return "foo";
    }
  }

  public static class BarFormatter implements Formatter {
    public String format() {
      return "bar";
    }
  }
}
