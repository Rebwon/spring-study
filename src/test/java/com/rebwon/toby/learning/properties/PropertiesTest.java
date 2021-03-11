package com.rebwon.toby.learning.properties;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Properties;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

public class PropertiesTest {

  @Test
  void propertiesFile() throws IOException {
    Properties properties = new Properties();
    properties.load(new ClassPathResource("test.properties").getInputStream());

    assertThat(properties.getProperty("name")).isEqualTo("rebwon");
    assertThat(properties.getProperty("age")).isEqualTo("23");
  }

  @Test
  void propertiesXml() throws IOException {
    Properties properties = new Properties();
    properties.loadFromXML(new ClassPathResource("test.xml").getInputStream());

    assertThat(properties.getProperty("name")).isEqualTo("리본");
    assertThat(properties.getProperty("age")).isEqualTo("23");
  }

  @Test
  void utilProperties() {
    GenericXmlApplicationContext ac = new GenericXmlApplicationContext(
        "context.xml");
    Properties p1 = ac.getBean("dbProperties", Properties.class);
    Properties p2 = ac.getBean("dbPropertiesXml", Properties.class);

    assertThat(p1.getProperty("name")).isEqualTo("rebwon");
    assertThat(p1.getProperty("age")).isEqualTo("23");
    assertThat(p2.getProperty("name")).isEqualTo("리본");
    assertThat(p2.getProperty("age")).isEqualTo("23");
  }

  @Test
  void environmentVar() {
    Properties properties = System.getProperties();
    for(Object key : properties.keySet()) {
      System.out.println(key);
    }
  }
}
