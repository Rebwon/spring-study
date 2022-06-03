package com.rebwon.toby;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Component
    static class FooService {

        @Autowired
        //@Qualifier("fooFormatter")
        private Formatter fooFormatter;
    }

    interface Formatter {
        String format();
    }

    @Component("fooFormatter")
    public class FooFormatter implements Formatter {

        public String format() {
            return "foo";
        }
    }

    @Component("barFormatter")
    public class BarFormatter implements Formatter {

        public String format() {
            return "bar";
        }
    }

}
