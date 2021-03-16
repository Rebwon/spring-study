package com.rebwon.toby.learning.spring.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import javax.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public class SimpleMVCTest extends AbstractDispatcherServletTest {

  @Test
  void simpleHandler() throws IOException, ServletException {
    this.setClasses(SimpleHandler.class, SimpleViewHandler.class)
        .runService("/hi");
    assertThat(this.response.getContentAsString()).isEqualTo("hi");

    this.runService("/view");
    assertThat(this.getModelAndView().getViewName()).isEqualTo("view.html");
  }

  @Controller
  static class SimpleHandler {
    @RequestMapping("/hi")
    @ResponseBody
    public String hi() { return "hi"; }
  }
  @Controller
  static class SimpleViewHandler {
    @RequestMapping("/view")
    public String view() { return "view.html"; }
  }

  @Test
  void contentNegotiation() throws ServletException, IOException {
    this.setClasses(TestAppConfig.class, ContentHandler.class)
        .runService("/content");

    assertThat(this.response.getContentType()).isEqualTo("application/json;charset=UTF-8");
    assertThat(this.response.getContentAsString()).isEqualTo("{\"name\":\"rebwon\"}");
  }

  @Controller
  static class ContentHandler {
    @RequestMapping("/content")
    public String content(ModelMap model) {
      model.put("name", "rebwon");
      return "content";
    }
  }
}
