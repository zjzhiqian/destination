package com.hzq.base.util;

import net.sf.cglib.core.ReflectUtils;
import org.springframework.beans.BeanUtils;

/**
 * Creator
 */
public class Creator {


    public static <T, S> T newInstance(S s, Class<T> c) {
        if (s == null) return null;
        T t = (T) ReflectUtils.newInstance(c);
        BeanUtils.copyProperties(s, t);
        return t;
    }

}
