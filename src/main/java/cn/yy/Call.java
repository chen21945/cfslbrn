package cn.yy;

import cn.yy.proxyip.BaseProxyIp;
import cn.yy.proxyip.CallProxyIp;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Created by Chengang on 2017/9/19.
 */
public class Call implements Runnable {

    private final long workId = 8149915;

    private static boolean flag = true;

    private static List<ProxyIp> ips;

    private void loadIps() {
        System.out.println("开始加载代理ip");
        BaseProxyIp proxyIp = new CallProxyIp();
        ips = proxyIp.get();
        if (ips == null || ips.size() == 0) {
            throw new RuntimeException();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        long time = System.currentTimeMillis();
        Call call = new Call();
        call.loadIps();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        while (flag) {
            if (System.currentTimeMillis() - time > (300 * 60 * 1000)) {
                flag = false;
                System.out.println("time end");
                break;
            }
            executor.execute(call);
            Thread.sleep(genSleepTime(new Random().nextInt(4)) * new Random().nextInt(10));
        }

        executor.shutdown();
    }

    private static int genSleepTime(int n) {
        return new Random().nextInt(1000) + n * 1000;
    }

    @Override
    public void run() {
        CallClient client = new CallClient("http://market.meihua.info/ajax/ajaxFunction.aspx");

        Map<String, Object> paramsSee = new HashMap<>();
        paramsSee.put("time", new Date());
        paramsSee.put("action", "addWorksView");
        paramsSee.put("worksID", this.workId);
        paramsSee.put("userName", "");
        paramsSee.put("source_path", "http://market.meihua.info/eventm/60660056");
        paramsSee.put("source_domain", "market.meihua.info");
        paramsSee.put("source_flag", "");
        paramsSee.put("provinceName", "");
        paramsSee.put("ip", null);


        ProxyIp ip = getRandomIp();
        if (ip == null) {
            return;
        }
        client.setProxt(ip.getIp(), ip.getPort().toString());
        //浏览
        paramsSee.put("time", new Date());
        paramsSee.put("ip", ip.getIp());
        paramsSee.put("provinceName", ip.getProvince());
        String result1 = client.get(paramsSee);
        if ("error".equals(result1)) {
            ip.setActive(false);
            System.out.println("failed，跳过");
            suc = false;
            return;
        } else {
            ip.setView(true);
            System.out.println("浏览成功，浏览" + ++t + "次");
            suc = true;
        }

        //喜爱
        if (!ip.isLike() && like()) {
            try {
                Thread.sleep(genSleepTime(10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Map<String, Object> paramsLike = new HashMap<>();
            paramsLike.put("time", new Date());
            paramsLike.put("action", "addPraise");
            paramsLike.put("worksID", this.workId);
            paramsLike.put("userName", "");
            String result = client.get(paramsLike);
            System.out.println(result);
            if (result == null || result.length() == 0 || result.equals("error")) {
                ip.setActive(false);
                System.out.println("failed，跳过");
            } else {
                ip.setLike(true);
                System.out.println("喜欢成功，喜欢" + ++l + "次");
            }
        }
    }

    private synchronized ProxyIp getRandomIp() {
        ips = ips.stream().filter(ip -> ip.isActive() && !ip.isView()).collect(Collectors.toList());
        int size = ips.size();
        if (size <= 0) {
            System.out.println("ip消耗完，重新获取ip");
            loadIps();
//            flag = false;
            return null;
        }
        int index = new Random().nextInt(size);
        return ips.get(index);
    }

    /**
     * 设定喜欢的概率是1/10
     *
     * @return
     */
    private boolean like() {
        int a = new Random().nextInt(15);
        if (a == 0) {
            return true;
        }
        return false;
    }

    private volatile int t;
    private volatile int l;
    private volatile boolean suc;
}
