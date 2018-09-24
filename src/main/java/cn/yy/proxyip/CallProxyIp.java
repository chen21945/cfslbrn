package cn.yy.proxyip;

import cn.yy.ProxyIp;
import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;


public class CallProxyIp extends BaseProxyIp {

    private String url = "http://api.goubanjia.com/api/get.shtml?order=c2d75d3f3d86c3f350f421fbaabfcf33&num=3000&area=%E4%B8%AD%E5%9B%BD&carrier=0&protocol=0&an1=1&an2=2&sp1=1&sp2=2&sort=1&system=1&rettype=0&seprator=%0D%0A&f_loc=1&f_anoy=1";

    public List<ProxyIp> get() {
        String result = this.sendRequest();
        if (result != null && result.length() > 0) {
            ResultData ipResult = JSON.parseObject(result, ResultData.class);
            if (!Boolean.TRUE.equals(ipResult.getSuccess())) {
                System.out.println("获取IP失败");
                return Collections.emptyList();
            }
            List<ProxyIp> ips = ipResult.getData();
            return ips;
        }
        return Collections.emptyList();
    }

    private String sendRequest() {

        CloseableHttpClient httpclient = HttpClients.createDefault();

        String result = "";
        try {
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpGet);

            HttpEntity entity = response.getEntity();
            if (entity != null)
                result = EntityUtils.toString(entity, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
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

}