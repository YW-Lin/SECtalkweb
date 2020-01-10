package com.wchat.boot;

import com.wchat.securitutils.Base64Utils;
import com.wchat.securitutils.RSAUtils;
import com.wchat.service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

@Component
@Log
public class KeyCache {
    public static RSAPublicKey publicKey;
    public static RSAPrivateKey privateKey;

    @Autowired
    UserService userService;
 
    @PostConstruct
    public void init() throws Exception {
        log.info("system init build SecurityKey");
        Map<String, Object> map = RSAUtils.genKeyPair();
        publicKey = (RSAPublicKey)map.get(RSAUtils.PUBLIC_KEY);
        privateKey = (RSAPrivateKey)map.get(RSAUtils.PRIVATE_KEY);
        if(publicKey==null||privateKey==null)   log.info("build SecurityKey fail!");

        userService.update4SystemInit();
    }
 
    @PreDestroy
    public void destroy() {
    }

}