package com.wchat.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CommonApi {

    public static String PW_AES_KEY;
    public static String TOKEN_AES_KEY;
    public static String pageSize;


    @Value("${PW_AES_KEY}")
    public void setPwAesKey(String pwAesKey) {
        PW_AES_KEY = pwAesKey;
    }
    @Value("${TOKEN_AES_KEY}")
    public void setTokenAesKey(String tokenAesKey) {
        TOKEN_AES_KEY = tokenAesKey;
    }
    @Value("${pageSize}")
    public void setPageSize(String pageSize) {
        CommonApi.pageSize = pageSize;
    }
}
