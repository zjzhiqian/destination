package com.hzq.test.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by hzq on 16/12/8.
 */
@SpringBootApplication
//@ImportResource("classpath:dubbo-consumer.xml")
public class TestApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(TestApplication.class, args);
    }
}
