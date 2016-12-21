package com.hzq.test.base.config;

import com.hzq.test.base.entity.ErrorResult;
import com.hzq.test.base.entity.RawString;
import com.hzq.test.base.entity.ReturnValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


//@ControllerAdvice
public class ReturnValueConverterAdvice implements ResponseBodyAdvice {


    private Logger logger = LoggerFactory.getLogger(ReturnValueConverterAdvice.class);


    public final boolean supports(final MethodParameter returnType, final Class converterType) {
        return true;
    }


    private Void printRawString(final ServerHttpResponse response, final String string) {
        try {
            response.getHeaders().setContentType(MediaType.TEXT_PLAIN);
            response.getBody().write(string.getBytes("US-ASCII"));
        } catch (Exception e) {
            logger.error("write raw string failed", e);
        }
        return null;
    }

    public final Object beforeBodyWrite(final Object body, final MethodParameter returnType,
                                        final MediaType selectedContentType, final Class selectedConverterType,
                                        final ServerHttpRequest request, final ServerHttpResponse response) {
        if (null == body)
            return new ReturnValue(true, null);
        else if (body instanceof ReturnValue)
            return body;
        else if (body instanceof ErrorResult)
            return new ReturnValue(false, body);
        else if (body instanceof RawString)
            return printRawString(response, body.toString());
        else
            return new ReturnValue(true, body);
    }
}
