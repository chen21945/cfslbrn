package cn.yy;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Chengang on 2017/9/26.
 */
@Getter
@Setter
public class ProxyIp implements Serializable {

    private String ip;
    private Integer port;
    private String country;
    private String province;
    private String city;
    private String anonymity;
    private String type;
    private String isp;
    private Long connectTimeMs;

    private boolean active = true;
    private boolean like = false;
    private boolean view = false;
    private static final List<String> PROVIENCE_NAME =
            Arrays.asList("北京市", "天津市", "上海市", "重庆市", "河北省", "山西省",
                    "辽宁省", "吉林省", "黑龙江省", "江苏省", "浙江省", "安徽省",
                    "福建省", "江西省", "山东省", "河南省", "湖北省", "湖南省",
                    "广东省", "海南省", "四川省", "贵州省", "云南省", "陕西省",
                    "甘肃省", "青海省", "台湾省", "内蒙古自治区", "广西壮族自治区",
                    "西藏自治区", "宁夏回族自治区", "新疆维吾尔自治区", "香港特别行政区", "澳门特别行政区");

    public ProxyIp(String ip, String port) {
        this.ip = ip;
        this.port = Integer.valueOf(port);
    }

    public ProxyIp() {

    }

    public String getProvience() {
        for (String name : PROVIENCE_NAME) {
            if (name.contains(province)) {
                return name;
            }
        }
        return "";
    }

}
