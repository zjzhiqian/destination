package com.hzq.account;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * Created by hzq on 16/12/13.
 */
public class AccountServiceProvider {
    static {
        BasicConfigurator.configure();
    }

    private static final Logger logger = Logger.getLogger(AccountServiceProvider.class);

    public static void main(String[] args) throws InterruptedException {
        logger.debug("213123123123123123123123");
        for (int i = 0; i < 100; i++) {
            TimeUnit.SECONDS.sleep(10);
            logger.debug("213123123123123123123123");
        }
        com.alibaba.dubbo.container.Main.main(args);
    }
}
