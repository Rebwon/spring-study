package com.rebwon.toby.learning.spring.spel;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class SpELTest {

  @Test
  void staticEx() {
    ExpressionParser parser = new SpelExpressionParser();
    Expression ex = parser.parseExpression("1+2");
    Integer result = (Integer) ex.getValue();
    assertThat(result).isEqualTo(3);
  }

  @Test
  void objectBinding() {
    User user = new User(1, "Spring", new Addr("Seoul", "Jongro"));
    ExpressionParser parser = new SpelExpressionParser();
    Expression ex = parser.parseExpression("name");
    assertThat(ex.getValue(user, String.class)).isEqualTo("Spring");
    assertThat(parser.parseExpression("addr.city").getValue(user, String.class)).isEqualTo("Seoul");
  }

  @Getter @Setter
  static class User {
    int id;
    String name;
    Addr addr;
    public User(int id, String name, Addr addr) {
      this.id = id;
      this.name = name;
      this.addr = addr;
    }
  }

  @Getter @Setter
  static class Addr {
    String city;
    String street;
    public Addr(String city, String street) {
      this.city = city;
      this.street = street;
    }
  }
}
