package com.wchat.bean;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.Date;

@Data
public class MsgLogBean {

    public MsgLogBean(Integer sendCode, String sendName, Integer acceptCode, String acceptName, String msg) {
        this.create = new Date();
        this.sendCode = sendCode;
        this.sendName = sendName;
        this.acceptCode = acceptCode;
        this.acceptName = acceptName;
        this.msg = msg;
    }

    public MsgLogBean() {
    }

    private Integer code;
    private Date create;
    private Integer sendCode;
    private String sendName;
    private Integer acceptCode;
    private String acceptName;
    private String msg;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
