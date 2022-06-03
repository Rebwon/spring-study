package com.rebwon.toby.learning.spring.ioc;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AnnotationConfigTest {

    static class Client {

        @Autowired
        Set<Service> beanBSet;
        @Autowired
        Service[] beanBArray;
        @Autowired
        Map<String, Service> beanBMap;
        @Autowired
        List<Service> beanBList;
        @Autowired
        Collection<Service> beanBCollection;
    }

    interface Service {

    }

    static class ServiceA implements Service {

    }

    static class ServiceB implements Service {

    }

    @Test
    void atAutowiredCollection() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(
            Client.class, ServiceA.class, ServiceB.class);
        Client client = ac.getBean(Client.class);
        assertThat(client.beanBArray.length).isEqualTo(2);
        assertThat(client.beanBSet.size()).isEqualTo(2);
        assertThat(client.beanBMap.entrySet().size()).isEqualTo(2);
        assertThat(client.beanBList.size()).isEqualTo(2);
        assertThat(client.beanBCollection.size()).isEqualTo(2);
    }

    @Test
    void atQualifier() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(QClient.class,
            QServiceA.class, QServiceB.class);
        QClient qclient = ac.getBean(QClient.class);
        assertThat(qclient.service.getClass()).isEqualTo(QServiceA.class);
    }

    static class QClient {

        @Autowired
        @Qualifier("main")
        Service service;
    }

    @Qualifier("main")
    static class QServiceA implements Service {

    }

    static class QServiceB implements Service {

    }

    @Test
    void atInject() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(IClient.class,
            IServiceA.class, IServiceB.class);
        IClient iclient = ac.getBean(IClient.class);
        assertThat(iclient.service.getClass()).isEqualTo(IServiceA.class);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Qualifier    // DIJ qualifier
    @interface Main {

    }

    static class IClient {

        @Inject
        @Main
        Service service;
    }

    @Main
    static class IServiceA implements Service {

    }

    static class IServiceB implements Service {

    }
}
