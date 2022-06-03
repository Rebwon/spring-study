package com.rebwon.toby.learning.proxy;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactoryBean;

public class DynamicProxyTest {

    @Test
    void simpleProxy() {
        Hello hello = new HelloTarget();
        assertThat(hello.sayHello("Rebwon")).isEqualTo("Hello Rebwon");
        assertThat(hello.sayHi("Rebwon")).isEqualTo("Hi Rebwon");
        assertThat(hello.sayThankYou("Rebwon")).isEqualTo("Thank you Rebwon");

        //Hello proxyHello = new HelloUppercase(hello);
        Hello proxyHello = (Hello) Proxy.newProxyInstance(
            getClass().getClassLoader(),
            new Class[]{Hello.class},
            new UppercaseHandler(new HelloTarget()));
        assertThat(proxyHello.sayHello("Rebwon")).isEqualTo("HELLO REBWON");
        assertThat(proxyHello.sayHi("Rebwon")).isEqualTo("HI REBWON");
        assertThat(proxyHello.sayThankYou("Rebwon")).isEqualTo("THANK YOU REBWON");
    }

    class UppercaseHandler implements InvocationHandler {

        Object target;

        public UppercaseHandler(Object target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object ret = method.invoke(target, args);
            if (ret instanceof String && method.getName().startsWith("say")) {
                return ((String) ret).toUpperCase();
            } else {
                return ret;
            }
        }
    }

    @Test
    void proxyFactoryBean() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());
        pfBean.addAdvice(new UppercaseAdvice());

        Hello proxyHello = (Hello) pfBean.getObject();
        assertThat(proxyHello.sayHello("Rebwon")).isEqualTo("HELLO REBWON");
        assertThat(proxyHello.sayHi("Rebwon")).isEqualTo("HI REBWON");
        assertThat(proxyHello.sayThankYou("Rebwon")).isEqualTo("THANK YOU REBWON");
    }

    class UppercaseAdvice implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation methodInvocation) throws Throwable {
            String ret = (String) methodInvocation.proceed();
            return ret.toUpperCase();
        }
    }

    interface Hello {

        String sayHello(String name);

        String sayHi(String name);

        String sayThankYou(String name);
    }

    class HelloTarget implements Hello {

        @Override
        public String sayHello(String name) {
            return "Hello " + name;
        }

        @Override
        public String sayHi(String name) {
            return "Hi " + name;
        }

        @Override
        public String sayThankYou(String name) {
            return "Thank you " + name;
        }
    }

    class HelloUppercase implements Hello {

        Hello hello;

        public HelloUppercase(Hello hello) {
            this.hello = hello;
        }

        @Override
        public String sayHello(String name) {
            return hello.sayHello(name).toUpperCase();
        }

        @Override
        public String sayHi(String name) {
            return hello.sayHi(name).toUpperCase();
        }

        @Override
        public String sayThankYou(String name) {
            return hello.sayThankYou(name).toUpperCase();
        }
    }
}
