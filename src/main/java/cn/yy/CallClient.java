package cn.yy;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;


public class CallClient {

    private String url = "";

    private HttpHost proxy;

    CallClient(String url) {
        this.url = url;
    }

    void setProxt(String ip, String port) {
        proxy = new HttpHost(ip, Integer.valueOf(port), "http");
        System.out.println("设置代理IP成功，ip=" + ip + ", port=" + port);
    }

    String get(Map<String, Object> map) {
        return this.sendRequest(map);
    }

    private String sendRequest(Map<String, Object> params) {

        CloseableHttpClient httpclient = HttpClients.createDefault();

        RequestConfig config = RequestConfig.custom()
                .setProxy(proxy)
                .build();
        String result = "";
        try {
            HttpGet httpGet = new HttpGet(getUrl(params));
            httpGet.setConfig(config);

            HttpResponse response = httpclient.execute(httpGet);

            HttpEntity entity = response.getEntity();
            if (entity != null)
                result = EntityUtils.toString(entity, "UTF-8");

            EntityUtils.consume(entity);
        } catch (IOException e) {
            return "error";
        } finally {
            if (httpclient != null)
                try {
                    httpclient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return result;
    }

    private String getUrl(Map<String, Object> params) {
        StringBuilder sb = new StringBuilder();
        params.forEach((key, value) -> {
            try {
                sb.append(key).append("=").append(value == null ? "" : URLEncoder.encode(String.valueOf(value), "UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("error");
            }
        });
        String newUrl;
        newUrl = url + "?" + sb.substring(0, sb.length() - 1);
//		System.out.println("开始like,url=" + newUrl);
        return newUrl;
    }

}