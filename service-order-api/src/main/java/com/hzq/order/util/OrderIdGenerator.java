package com.hzq.order.util;

import org.springframework.util.IdGenerator;
import org.springframework.util.SimpleIdGenerator;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hzq on 16/12/13.
 */
public class OrderIdGenerator {

    private static final IdGenerator idGenerator = new SimpleIdGenerator();

    public static String getOrderNo() {
        String uuid = idGenerator.generateId().toString();
        uuid = uuid.substring(uuid.lastIndexOf("-") + 1); //12位
        return new SimpleDateFormat("MMddHHmmssSSS").format(new Date()) + uuid;
    }
}
