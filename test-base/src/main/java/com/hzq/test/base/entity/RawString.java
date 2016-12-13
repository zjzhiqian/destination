package com.hzq.test.base.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * Created by hzq on 16/12/11.
 */
@JsonSerialize(using = ToStringSerializer.class)
public final class RawString {
    private String string;

    public RawString(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }
}

