package com.wchat.bean;

import java.util.LinkedHashMap;

public class VerifiCodeCacheBean {

    private VerifiCodeCacheBean() {
    }

    private LinkedHashMap<String, String> map;

    private static final VerifiCodeCacheBean bean;

    static {
        bean = new VerifiCodeCacheBean();
        bean.map = new LinkedHashMap<String,String>();
    }

    public static VerifiCodeCacheBean getCache(){
        return bean;
    }

    public LinkedHashMap<String, String> getMap() {
        return map;
    }

    public void setMap(LinkedHashMap<String, String> map) {
        this.map = map;
    }
}
