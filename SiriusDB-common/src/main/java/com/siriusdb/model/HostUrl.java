package com.siriusdb.model;

import lombok.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: 主机目标的数据结构
 * @author: liuxuanming
 * @date: 2021/05/16 2:33 下午
 */
@Data
public class HostUrl {

    private String ip;

    private Integer port;

    private static final String HOST_URL_REGEX = "(\\d+\\.\\d+\\.\\d+\\.\\d+)\\:(\\d+)";

    public static HostUrl parseHostUrl(String hostUrl) {
        HostUrl result = new HostUrl();

        if(hostUrl == null && hostUrl.equals("")) return null;
        else if(!hostUrl.matches(HOST_URL_REGEX)) return null;
        else {
            result.setIp(hostUrl.split(":")[0]);
            result.setPort(Integer.parseInt(hostUrl.split(":")[1]));
        }
        return result;
    }
}
