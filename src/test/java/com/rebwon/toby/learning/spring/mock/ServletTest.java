package com.rebwon.toby.learning.spring.mock;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import javax.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class ServletTest {

    @Test
    void getMethodServlet() throws ServletException, IOException {
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/hello");
        req.addParameter("name", "rebwon");
        MockHttpServletResponse res = new MockHttpServletResponse();

        SimpleGetServlet servlet = new SimpleGetServlet();
        servlet.service(req, res);
        servlet.init();

        assertThat(res.getContentAsString()).isEqualTo("<HTML><BODY>Hello rebwon</BODY></HTML>");
        assertThat(res.getContentAsString().indexOf("Hello rebwon") > 0).isTrue();
    }
}
