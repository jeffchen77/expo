package com.xiaoi.expo.common.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author bright.liang
 * @Description: httpClient公共类，用于通过http请求调用外部接口
 * @date 2018/4/410:59
 */
public class HttpClientUtils {

    /**
     * get方式调用外部接口
     *
     * @param url 接口地址
     * @return String
     * @method get
     */
    public static String get(String url) throws Exception {
        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity);
                }
            } finally {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }

        return result;
    }
}
