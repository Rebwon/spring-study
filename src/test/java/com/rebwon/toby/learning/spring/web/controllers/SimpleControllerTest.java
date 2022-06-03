package com.rebwon.toby.learning.spring.web.controllers;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.rebwon.toby.learning.spring.web.AbstractDispatcherServletTest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class SimpleControllerTest extends AbstractDispatcherServletTest {

    @Test
    @Disabled
    void helloController() throws ServletException, IOException {
        setClasses(HelloController.class);
        initRequest("/hello").addParameter("name", "Spring");
        runService();
        assertModel("message", "Hello Spring");
        assertViewName("/src/main/resources/static/hello.html");
    }

    @RequestMapping("/hello")
    static class HelloController extends SimpleController {

        public HelloController() {
            this.setRequiredParams(new String[]{"name"});
            this.setViewName("/src/main/resources/static/hello.html");
        }

        @Override
        public void control(Map<String, String> params, Map<String, Object> model)
            throws Exception {
            model.put("message", "Hello " + params.get("name"));
        }
    }

    static abstract class SimpleController implements Controller {

        private String[] requiredParams;
        private String viewName;

        public void setRequiredParams(String[] requiredParams) {
            this.requiredParams = requiredParams;
        }

        public void setViewName(String viewName) {
            this.viewName = viewName;
        }

        @Override
        final public ModelAndView handleRequest(HttpServletRequest request,
            HttpServletResponse response)
            throws Exception {
          if (viewName == null) {
            throw new IllegalStateException();
          }

            Map<String, String> params = new HashMap<>();
            for (String param : requiredParams) {
                String value = request.getParameter(param);
              if (value == null) {
                throw new IllegalStateException();
              }
                params.put(param, value);
            }

            Map<String, Object> model = new HashMap<>();

            this.control(params, model);
            return new ModelAndView(this.viewName, model);
        }

        public abstract void control(Map<String, String> params, Map<String, Object> model)
            throws Exception;
    }
}
