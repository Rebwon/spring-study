package com.rebwon.toby.learning.spring.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AbstractRefreshableWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;

public class ConfigurableDispatcherServlet extends DispatcherServlet {
  protected Class<?>[] classes;
  private String[] locations;

  private ModelAndView modelAndView;

  public ConfigurableDispatcherServlet() {}

  public ConfigurableDispatcherServlet(String[] locations) {
    this.locations = locations;
  }

  public ConfigurableDispatcherServlet(Class<?>[] classes) {
    this.classes = classes;
  }

  public void setLocations(String... locations) {
    this.locations = locations;
  }

  public void setRelativeLocations(Class clazz, String... relativeLocations) {
    String[] locations = new String[relativeLocations.length];
    String currentPath = ClassUtils.classPackageAsResourcePath(clazz) + "/";
    for(int i=0; i<relativeLocations.length; i++) {
      locations[i] = currentPath + relativeLocations[i];
    }
    this.setLocations(locations);
  }

  public void setClasses(Class<?>... classes) {
    this.classes = classes;
  }

  @Override
  public void service(ServletRequest req, ServletResponse res)
      throws ServletException, IOException {
    this.modelAndView = null;
    super.service(req, res);
  }

  @Override
  protected WebApplicationContext createWebApplicationContext(ApplicationContext parent) {
    AbstractRefreshableWebApplicationContext wac = new AbstractRefreshableWebApplicationContext() {

      @Override
      protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory)
          throws BeansException, IOException {
        if(locations != null) {
          XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(
              beanFactory);
          xmlReader.loadBeanDefinitions(locations);
        }
        AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(
            beanFactory);
        if(classes != null) {
          reader.register(classes);
        }
      }
    };

    wac.setServletContext(getServletContext());
    wac.setServletConfig(getServletConfig());
    wac.refresh();
    return wac;
  }

  @Override
  protected void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    this.modelAndView = mv;
    super.render(mv, request, response);
  }

  public ModelAndView getModelAndView() {
    return modelAndView;
  }
}
