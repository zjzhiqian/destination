package com.hzq.base.util;

import java.util.UUID;

/**
 * Created by hzq on 16/12/12.
 */
public class UUIDUtils {

    public static String get32UUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
