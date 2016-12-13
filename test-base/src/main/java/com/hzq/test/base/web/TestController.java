package com.hzq.test.base.web;

import com.hzq.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * Created by hzq on 16/12/13.
 */
@RestController
@RequestMapping("a")
public class TestController {

    @Autowired
    AccountService accountService;

    @GetMapping("b")
    public void sout() {
        accountService.addAmountToMerchant(1, BigDecimal.ONE, "123", "321");
    }
}
