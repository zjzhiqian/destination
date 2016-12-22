package com.hzq.springboot.test;

import com.hzq.base.util.UUIDUtils;
import com.hzq.order.util.OrderIdGenerator;
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
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by hzq on 16/12/12.
 */
public class MainClass {
    private static final CloseableHttpClient client = HttpClients.custom().build();


    public static void main(String[] args) throws IOException, InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        ExecutorService service2 = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        ExecutorService service3 = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        ExecutorService service4 = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (int i = 0; i < 1250; i++) {
            service.execute(new Task());
            service2.execute(new Task());
            service3.execute(new Task());
            service4.execute(new Task());
        }
    }

    private static class Task implements Runnable {

        @Override
        public void run() {
            try {
                String orderNo = OrderIdGenerator.getOrderNo();
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("productId", "1"));
                params.add(new BasicNameValuePair("orderAmount", "10"));
                params.add(new BasicNameValuePair("payWay", "3"));
                params.add(new BasicNameValuePair("orderNo", orderNo));
                params.add(new BasicNameValuePair("orderIp", "127.0.0.1"));
                params.add(new BasicNameValuePair("orderPeriod", "23"));
//        params.add(new BasicNameValuePair("expireTime", "1481472188361"));
                params.add(new BasicNameValuePair("returnUrl", "returnUrl"));
                params.add(new BasicNameValuePair("notifyUrl", "notifyUrl"));
                params.add(new BasicNameValuePair("remark", "remark"));
                HttpPost post = new HttpPost("http://localhost:8081/api/v3/orders/pay");
                post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                CloseableHttpResponse response = client.execute(post);
                InputStream content = response.getEntity().getContent();
                String s = IOUtils.toString(content);
                response.close();
                System.out.println(s);
                TimeUnit.SECONDS.sleep(1);
                post = new HttpPost("http://localhost:8081/api/v3/orderNotify/notify");
                params = new ArrayList<>();
                params.add(new BasicNameValuePair("resultCode", "success"));
                params.add(new BasicNameValuePair("transactionId", UUIDUtils.get32UUID()));
                params.add(new BasicNameValuePair("timeEnd", new Date().toString()));
                params.add(new BasicNameValuePair("outTradeNo", orderNo));
                post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                response = client.execute(post);
                s = IOUtils.toString(content);
                response.close();
                System.out.println(s);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
