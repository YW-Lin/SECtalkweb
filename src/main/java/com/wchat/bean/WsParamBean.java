package com.wchat.bean;

import com.alibaba.fastjson.JSON;
import com.wchat.utils.CommonApi;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class WsParamBean {

    public WsParamBean(String sessionId, Integer msgType) {
        this.sessionId = sessionId;
        this.msgType = msgType;
    }

    public WsParamBean(String pkey, String pdata) {
        this.pkey = pkey;
        this.pdata = pdata;
    }

    public WsParamBean() {
    }

    private Integer code;
    private String msg;
    private String sessionId;
    private String userName;
    private String pw;
    private String newPw;
    private String pkey;
    private String pdata;
    private String token;
    private String otherToken;
    private Integer otherCode;
    private String email;

    private String uuid;

    private String verifiCode;

    private Integer status;//0 offline 1 line

    private Integer pageNo;
    private Integer pageSize;

    private Integer msgType = msgType_caht;
    public static Integer msgType_caht = 1;
    public static Integer msgType_sys = 2;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public Integer getPageSize() {
        return this.pageSize==null? Integer.valueOf(CommonApi.pageSize):this.pageSize;
    }

    public Integer getPageNo() {
        return pageNo==null?1:pageNo;
    }
}
