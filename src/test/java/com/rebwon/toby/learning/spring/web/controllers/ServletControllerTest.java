package com.rebwon.toby.learning.spring.web.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import com.rebwon.toby.learning.spring.web.AbstractDispatcherServletTest;
import java.io.IOException;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.SimpleServletHandlerAdapter;

public class ServletControllerTest extends AbstractDispatcherServletTest {

    @Test
    void helloServletController() throws ServletException, IOException {
        setClasses(SimpleServletHandlerAdapter.class, HelloServlet.class);
        initRequest("/hello").addParameter("name", "Spring");
        assertThat(runService().getContentAsString()).isEqualTo("Hello Spring");
    }

    /*@Component("/hello")*/
    @Named("/hello")
    static class HelloServlet extends HttpServlet {

        // DispatcherServlet은 다음과 같이 Servlet 클래스를 지원하기 때문에,
        // ModelAndView에 null을 리턴하면, 뷰 호출을 생략하고 작업을 종료한다.
        protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
            String name = req.getParameter("name");
            resp.getWriter().print("Hello " + name);
        }
    }
}
