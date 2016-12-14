package com.hzq.accounting;

import com.hzq.accounting.entity.Accounting;
import com.hzq.accounting.service.AccountingService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hzq on 16/12/13.
 */
public class AccountingServiceProvider {
    public static void main(String[] args) {
        com.alibaba.dubbo.container.Main.main(args);
//
//        try {
//            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:application-spring.xml");
//            context.start();
//
//
//
//            AccountingService service = context.getBean("accountingService", AccountingService.class);
//
//            ExecutorService pool = Executors.newFixedThreadPool(5);
//            Accounting accounting = new Accounting();
//            accounting.setVoucherNo("123213");
//
//            pool.execute(()-> service.generateAccounting(accounting));
//            pool.execute(()-> service.generateAccounting(accounting));
//            pool.execute(()-> service.generateAccounting(accounting));
//            pool.execute(()-> service.generateAccounting(accounting));
//            pool.execute(()-> service.generateAccounting(accounting));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//

    }
}
