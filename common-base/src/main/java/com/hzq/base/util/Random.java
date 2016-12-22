package com.hzq.base.util;

/**
 * Created by hzq on 16/12/22.
 */
public class Random {
    public static void randomError() {
        java.util.Random random = new java.util.Random();
        if (random.nextBoolean() && random.nextBoolean() && random.nextBoolean() && random.nextBoolean()) {
            throw new RuntimeException("randomError");
        }
    }
}
