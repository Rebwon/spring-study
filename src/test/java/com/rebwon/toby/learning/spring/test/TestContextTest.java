package com.rebwon.toby.learning.spring.test;

import static org.assertj.core.api.Assertions.assertThat;

import com.rebwon.toby.learning.spring.test.TestContextTest.TestContextConfig;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestContextConfig.class)
public class TestContextTest {

    @Autowired
    BeanA a;
    @Resource
    BeanA beanA;
    BeanA b;
    BeanA c;

    @Autowired
    public void setBeanA(BeanA b) {
        this.b = b;
    }

    @Autowired
    public void init(BeanA c) {
        this.c = c;
    }

    @Test
    @DirtiesContext
    void test1() {
        assertThat(a).isNotNull();
        assertThat(beanA).isNotNull();
        assertThat(b).isNotNull();
        assertThat(c).isNotNull();
    }

    @Configuration
    static class TestContextConfig {

        @Bean
        public BeanA beanA() {
            return new BeanA();
        }
    }

    static class BeanA {

        @PostConstruct
        public void init() {
            System.out.println("A");
        }
    }
}
