package com.wchat.bean;

import lombok.Data;

@Data
public class TokenBean {

    public TokenBean(Integer code, String rd) {
        this.code = code;
        this.rd = rd;
    }

    public TokenBean() {
    }

    private Integer code;

    private String rd;

    public boolean isComplete(){
        if(code==null)  return false;
        if(rd==null||rd.equals(""))  return false;
        return true;
    }

    public boolean verifyToken4User(WsParamBean bean){
        if(bean==null)    return false;
        if(bean.getUuid()==null||bean.getUuid().equals("")) return false;
        if(bean.getUuid().equals(this.rd))  return true;
        return false;
    }
}
