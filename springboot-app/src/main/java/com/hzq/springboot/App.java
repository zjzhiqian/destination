package com.hzq.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by hzq on 16/12/8.
 */
@SpringBootApplication
@ImportResource("classpath:dubbo.xml")
public class App {
    public static void main(String[] args) throws Exception {
        System.out.println(1);
        SpringApplication.run(App.class, args);
    }
}
