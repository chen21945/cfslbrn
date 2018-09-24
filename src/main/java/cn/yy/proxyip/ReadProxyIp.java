package cn.yy.proxyip;

import cn.yy.ProxyIp;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chengang on 2017/9/26.
 */
public class ReadProxyIp extends BaseProxyIp {

    private File file;

    public List<ProxyIp> get() {
        List<ProxyIp> proxyIps = new ArrayList<>();
        try {
            file = new File("ips");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String tmp = null;
            ProxyIp proxyIp = null;
            while ((tmp = reader.readLine()) != null) {
                String[] strArr = tmp.split(":");
                proxyIp = new ProxyIp(strArr[0], strArr[1]);
                proxyIps.add(proxyIp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return proxyIps;
    }

    public static void main(String[] args) throws IOException {
        ReadProxyIp read = new ReadProxyIp();
        read.get();
    }


}
