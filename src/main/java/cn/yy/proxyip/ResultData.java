package cn.yy.proxyip;

import cn.yy.ProxyIp;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by yang_ on 2018/9/21.
 */
@Getter
@Setter
public class ResultData {

    private Boolean success;

    private String msg;

    private List<ProxyIp> data;

}
