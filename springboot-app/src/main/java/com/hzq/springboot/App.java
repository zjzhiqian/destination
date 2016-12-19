package com.hzq.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by hzq on 16/12/8.
 */
@SpringBootApplication
@ImportResource("classpath:application-dubbo-consumer.xml")
public class App {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(App.class, args);
    }
}
