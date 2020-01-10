package com.wchat.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wchat.bean.TokenBean;
import com.wchat.bean.WsParamBean;
import com.wchat.boot.KeyCache;
import com.wchat.securitutils.AESUtils;
import com.wchat.securitutils.Base64Utils;
import com.wchat.securitutils.RSAUtils;
import lombok.extern.java.Log;

import java.security.GeneralSecurityException;
import java.util.UUID;

@Log
public class SecurityUtil {

    public static String encryptMsg(String msg) {
        try {
            String aesPw = getUUID(12);
            String pdata = AESUtils.encrypt(aesPw, msg);
            byte[] encrypt = RSAUtils.encryptByPrivateKey(Base64Utils.decode(aesPw), Base64Utils.encode(KeyCache.privateKey.getEncoded()));
            String pkey = Base64Utils.encode(encrypt);
            return new WsParamBean(pkey,pdata).toString();
        } catch (Exception e) {
            log.info("Encryption failed!");
            e.printStackTrace();
        }
        return null;
    }

    public static String encryptPw(String pw){
        try {
            return AESUtils.encrypt(pw, CommonApi.PW_AES_KEY);
        } catch (Exception e) {
            log.info("pw build fail:"+e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject buildToken(Integer code){
        String uuid = getUUID(8);
        TokenBean tokenBean = new TokenBean(code, uuid);
        JSONObject rejson = new JSONObject();
        try {
            rejson.put("token",AESUtils.encrypt(CommonApi.TOKEN_AES_KEY,JSON.toJSONString(tokenBean)));
            rejson.put("uuid",uuid);
        } catch (GeneralSecurityException e) {
            log.info("token build fail:"+e.getMessage());
            e.printStackTrace();
        }
        return rejson;

    }
    public static String getUUID(Integer length){
        String replace = UUID.randomUUID().toString().replace("-", "");
        return replace.substring(replace.length()-length<0?0:replace.length()-length);
    }

    public static TokenBean decryptToken(String token) {
        try {
            return JSON.parseObject(AESUtils.decrypt(CommonApi.TOKEN_AES_KEY,token),TokenBean.class);
        } catch (GeneralSecurityException e) {
            log.info("token decrypt fail:"+e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
