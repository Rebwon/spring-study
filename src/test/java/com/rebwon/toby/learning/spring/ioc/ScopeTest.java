package com.rebwon.toby.learning.spring.ioc;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ObjectFactoryCreatingFactoryBean;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AbstractRefreshableWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class ScopeTest {

  @Test
  void singletonScope() {
    ApplicationContext ac = new AnnotationConfigApplicationContext(SingletonBean.class, SingletonClientBean.class);

    Set<SingletonBean> bean = new HashSet<>();
    bean.add(ac.getBean(SingletonBean.class));
    bean.add(ac.getBean(SingletonBean.class));
    assertThat(bean.size()).isEqualTo(1);

    bean.add(ac.getBean(SingletonClientBean.class).bean1);
    bean.add(ac.getBean(SingletonClientBean.class).bean2);
    assertThat(bean.size()).isEqualTo(1);
  }

  static class SingletonBean {}
  static class SingletonClientBean {
    @Autowired
    SingletonBean bean1;
    @Autowired	SingletonBean bean2;
  }

  @Test
  void prototypeScope() {
    ApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class, PrototypeClientBean.class);

    Set<PrototypeBean> bean = new HashSet<>();
    bean.add(ac.getBean(PrototypeBean.class));
    assertThat(bean.size()).isEqualTo(1);
    bean.add(ac.getBean(PrototypeBean.class));
    assertThat(bean.size()).isEqualTo(2);

    bean.add(ac.getBean(PrototypeClientBean.class).bean1);
    assertThat(bean.size()).isEqualTo(3);
    bean.add(ac.getBean(PrototypeClientBean.class).bean2);
    assertThat(bean.size()).isEqualTo(4);
  }

  @Component("prototypeBean")
  @Scope("prototype")
  static class PrototypeBean {}
  static class PrototypeClientBean {
    @Autowired	PrototypeBean bean1;
    @Autowired	PrototypeBean bean2;
  }

  @Test
  void objectFactory() {
    ApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class, ObjectFactoryConfig.class);
    ObjectFactory<PrototypeBean> factoryBeanFactory = ac.getBean("prototypeBeanFactory", ObjectFactory.class);

    Set<PrototypeBean> bean = new HashSet<>();
    for(int i=1; i<=4; i++) {
      bean.add(factoryBeanFactory.getObject());
      assertThat(bean.size()).isEqualTo(i);
    }
  }

  @Configuration
  static class ObjectFactoryConfig {
    @Bean
    public ObjectFactoryCreatingFactoryBean prototypeBeanFactory() {
      ObjectFactoryCreatingFactoryBean factoryBean = new ObjectFactoryCreatingFactoryBean();
      factoryBean.setTargetBeanName("prototypeBean");
      return factoryBean;
    }
  }

  @Test
  void serviceLocatorFactoryBean() {
    ApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class, ServiceLocatorConfig.class);
    PrototypeBeanFactory factory = ac.getBean(PrototypeBeanFactory.class);

    Set<PrototypeBean> bean = new HashSet<>();
    for(int i=1; i<=4; i++) {
      bean.add(factory.getPrototypeBean());
      assertThat(bean.size()).isEqualTo(i);
    }
  }

  interface PrototypeBeanFactory { PrototypeBean getPrototypeBean(); }
  @Configuration
  static class ServiceLocatorConfig {
    @Bean public ServiceLocatorFactoryBean prototypeBeanFactory() {
      ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
      factoryBean.setServiceLocatorInterface(PrototypeBeanFactory.class);
      return factoryBean;
    }
  }

  @Test
  void providerTest() {
    ApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class, ProviderClient.class);
    ProviderClient client = ac.getBean(ProviderClient.class);

    Set<PrototypeBean> bean = new HashSet<>();
    for(int i=1; i<=4; i++) {
      bean.add(client.prototypeBeanProvider.get());
      assertThat(bean.size()).isEqualTo(i);
    }
  }

  static class ProviderClient {
    @Inject
    Provider<PrototypeBean> prototypeBeanProvider;
  }

  MockHttpServletResponse res = new MockHttpServletResponse();

  @Test
  void requestScope() throws ServletException, IOException {
    MockServletConfig ctx = new MockServletConfig(new MockServletContext(), "spring");
    DispatcherServlet ds = new AnnotationConfigDispatcherServlet(
        HelloController.class, HelloService.class, RequestBean.class, BeanCounter.class);
    ds.init(ctx);

    BeanCounter counter = ds.getWebApplicationContext().getBean(BeanCounter.class);

    ds.service(new MockHttpServletRequest("GET", "/hello"), this.res);
    assertThat(counter.addCounter).isEqualTo(2);
    assertThat(counter.size()).isEqualTo(1);

    ds.service(new MockHttpServletRequest("GET", "/hello"), this.res);
    assertThat(counter.addCounter).isEqualTo(4);
    assertThat(counter.size()).isEqualTo(2);

    for(String name :
        ((AbstractRefreshableWebApplicationContext) ds.getWebApplicationContext()).getBeanFactory().getRegisteredScopeNames()
    ) {
      System.out.println(name);
    }
  }

  static class AnnotationConfigDispatcherServlet extends DispatcherServlet {
    private Class<?>[] classes;

    public AnnotationConfigDispatcherServlet(Class<?> ...classes) {
      super();
      this.classes = classes;
    }

    @Override
    protected WebApplicationContext createWebApplicationContext(WebApplicationContext parent) {
      AbstractRefreshableWebApplicationContext wac = new AbstractRefreshableWebApplicationContext() {

        @Override
        protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory)
            throws BeansException, IOException {
          AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(
              beanFactory);
          reader.register(classes);
        }
      };
      wac.setServletConfig(getServletConfig());
      wac.setServletContext(getServletContext());
      wac.refresh();
      return wac;
    }
  }

  @RequestMapping("/")
  static class HelloController {
    @Autowired HelloService helloService;
    @Autowired Provider<RequestBean> requestBeanProvider;
    @Autowired BeanCounter beanCounter;

    @RequestMapping("hello")
    public String hello() {
      beanCounter.addCounter++;
      beanCounter.add(requestBeanProvider.get());
      helloService.hello();
      return "";
    }
  }

  static class HelloService {
    @Autowired Provider<RequestBean> requestBeanProvider;
    @Autowired BeanCounter beanCounter;
    public void hello() {
      beanCounter.addCounter++;
      beanCounter.add(requestBeanProvider.get());
    }
  }

  @Scope(value="request")
  static class RequestBean {}
  static class BeanCounter extends HashSet { int addCounter = 0; }
}
