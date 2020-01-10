package com.wchat.controller;

import com.wchat.bean.ResultBean;
import com.wchat.boot.KeyCache;
import com.wchat.securitutils.AESUtils;
import com.wchat.securitutils.Base64Utils;
import com.wchat.securitutils.RSAUtils;
import com.wchat.utils.CommonApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/api/rsa")
@RestController
public class RSAController extends BaseController{

    @RequestMapping(value="/getPublicKey", method= RequestMethod.POST)
    public ResultBean getPublicKey() throws Exception {
        return super.getSuccResult4Data(Base64Utils.encode(KeyCache.publicKey.getEncoded()));
    }

    @RequestMapping(value="/de4privateKey", method= RequestMethod.POST)
    public ResultBean de4privateKey(String pkey,String pdata) {
        String tdata;
        try {
            byte[] tkey = RSAUtils.decryptByPrivateKey(Base64Utils.decode(pkey), Base64Utils.encode(KeyCache.privateKey.getEncoded()));
            tdata = AESUtils.decrypt(new String(tkey,"UTF-8"), pdata);
        } catch (Exception e) {
            e.printStackTrace();
            return super.getFailResult("Decryption failed！");
        }
        return super.getSuccResult4Data(tdata);
    }


    @RequestMapping(value="/de4publicKey", method= RequestMethod.POST)
    public ResultBean de4publicKey(String pkey,String pdata) {
        String tdata;
        try {
            byte[] tkey = RSAUtils.decryptByPublicKey(Base64Utils.decode(pkey), Base64Utils.encode(KeyCache.publicKey.getEncoded()));
            tdata = AESUtils.decrypt(Base64Utils.encode(tkey), pdata);
        } catch (Exception e) {
            e.printStackTrace();
            return super.getFailResult("Decryption failed！");
        }
        return super.getSuccResult4Data(tdata);
    }
}
