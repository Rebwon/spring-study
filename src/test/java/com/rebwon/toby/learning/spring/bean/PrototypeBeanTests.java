package com.rebwon.toby.learning.spring.bean;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

public final class PrototypeBeanTests {

    // 싱글톤 빈에서 프로토 타입 빈을 참조하면
    // getBean을 요청할 때마다 새롭게 생성하는 프로토 타입 빈의
    // 특성이 사라지고, 싱글톤 빈의 라이프 사이클에 맞춰서
    // 이미 주입된 프로토 타입 빈은 새로 생성되지 않는다.
    // 따라서 count라는 가변 상태를 가지고 있기 때문에
    // logic을 두 번 호출할 경우 count 값이 2가 된다.
    @Test
    void singletonClientUsePrototype() {
        AnnotationConfigApplicationContext ac = new
            AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);
        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        assertThat(count1).isEqualTo(1);
        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int count2 = clientBean2.logic();
        assertThat(count2).isEqualTo(2);
    }

    static class ClientBean {

        private final PrototypeBean prototypeBean;

        @Autowired
        public ClientBean(PrototypeBean prototypeBean) {
            this.prototypeBean = prototypeBean;
        }

        public int logic() {
            prototypeBean.addCount();
            int count = prototypeBean.getCount();
            return count;
        }
    }

    @Scope("prototype")
    static class PrototypeBean {

        private int count = 0;

        public void addCount() {
            count++;
        }

        public int getCount() {
            return count;
        }

        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init " + this);
        }

        @PreDestroy
        public void destroy() {
            System.out.println("PrototypeBean.destroy");
        }
    }
}
