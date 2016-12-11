package com.hzq.springboot.test;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzq on 16/12/12.
 */
public class MainClass {
    private static final CloseableHttpClient client = HttpClients.custom().build();

    public static void main(String[] args) throws IOException {
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("productId", "1"));
        params.add(new BasicNameValuePair("orderAmount", "20"));
        params.add(new BasicNameValuePair("payWay", "3"));
        params.add(new BasicNameValuePair("orderNo", "100"));
        params.add(new BasicNameValuePair("orderIp", "127.0.0.1"));
        params.add(new BasicNameValuePair("orderPeriod", "23"));
//        params.add(new BasicNameValuePair("expireTime", "1481472188361"));
        params.add(new BasicNameValuePair("returnUrl", "returnUrl"));
        params.add(new BasicNameValuePair("notifyUrl", "notifyUrl"));
        params.add(new BasicNameValuePair("remark", "remark"));
        HttpPost post = new HttpPost("http://localhost:8080/api/v3/orders/pay");
        post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        CloseableHttpResponse response = client.execute(post);
        InputStream content = response.getEntity().getContent();
        String s = IOUtils.toString(content);
        response.close();
        System.out.println(s);
    }
}
