package com.hzq.mq;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by hzq on 16/12/10.
 */
public class MQApplication {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application-spring.xml");
        synchronized (MQApplication.class) {
            while (true) {
                try {
                    MQApplication.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
