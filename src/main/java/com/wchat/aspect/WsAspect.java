package com.wchat.aspect;

import com.alibaba.fastjson.JSON;
import com.wchat.bean.ResultBean;
import com.wchat.bean.WsParamBean;
import com.wchat.boot.KeyCache;
import com.wchat.securitutils.AESUtils;
import com.wchat.securitutils.Base64Utils;
import com.wchat.securitutils.RSAUtils;
import com.wchat.utils.XssUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class WsAspect {

	@Pointcut("execution(* com.wchat.controller.WebSocketController.*(..))")
	public void print(){
	}

	/**
	 * @param point
	 */
	@Around("print()")
	public Object wsAround(ProceedingJoinPoint point){
		String declaringTypeName = point.getSignature().getDeclaringTypeName();
		String methodName = point.getSignature().getName();
		log.info("Raw parameter："+point.getArgs()[0]);
		WsParamBean bean = (WsParamBean)point.getArgs()[0];
		String pkey = bean.getPkey();
		String pdata = bean.getPdata();
		try {

			byte[] tkey = RSAUtils.decryptByPrivateKey(Base64Utils.decode(pkey), Base64Utils.encode(KeyCache.privateKey.getEncoded()));
//			String tdata = AESUtils.decrypt(Base64Utils.encode(tkey), pdata);
			String tdata = AESUtils.decrypt(new String(tkey,"UTF-8"), pdata);

			tdata = XssUtils.XSSHtmlFilt(tdata);
			WsParamBean wsParamBean = JSON.parseObject(tdata, WsParamBean.class);
			return point.proceed(new Object[]{wsParamBean});
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultBean("500","Decryption failed！",null);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			return new ResultBean("500","Decryption failed！",null);
		}
	}


}
