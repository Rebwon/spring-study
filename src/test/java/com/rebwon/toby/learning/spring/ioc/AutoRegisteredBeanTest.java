package com.rebwon.toby.learning.spring.ioc;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.Properties;
import javax.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.ResourceLoader;

public class AutoRegisteredBeanTest {

    @Test
    void autoRegisteredBean() {
        System.getProperties().put("os.name", "Hi");

        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(
            SystemBean.class);
        SystemBean bean = ac.getBean(SystemBean.class);
        assertThat(bean.applicationContext).isEqualTo(ac);

        System.out.println(bean.osname);
        System.out.println(bean.path);

        System.out.println("### " + bean.systemProperties);
        System.out.println("$$$ " + bean.systemEnvironment);
    }

    static class SystemBean {

        @Resource
        ApplicationContext applicationContext;
        @Autowired
        BeanFactory beanFactory;
        @Autowired
        ResourceLoader resourceLoader;
        @Autowired
        ApplicationEventPublisher applicationEventPublisher;

        @Value("#{systemProperties['os.name']}")
        String osname;
        @Value("#{systemEnvironment['path']}")
        String path;

        @Resource
        Properties systemProperties;
        @Resource
        Map systemEnvironment;
    }
}
