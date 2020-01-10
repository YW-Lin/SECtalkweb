package com.wchat.service;

import com.wchat.bean.MsgLogBean;
import com.wchat.bean.WsParamBean;

import java.util.List;

public interface UserService {
    WsParamBean getUserByToken(String token);

    int insertUser(WsParamBean bean);

    boolean checkUserName(String userName);

    List<WsParamBean> queryUserByUserName(String userName);

    boolean checkPw(WsParamBean bean);

    int update4Login(WsParamBean bean);

    boolean checkToken(String token);

    WsParamBean getUserByCode(Integer code);

    int update4Offline(WsParamBean bean);

    int insertMsgLog(MsgLogBean msgLogBean);

    void update4SystemInit();

    List<WsParamBean> queryUserByStatus(Integer status, Integer code);

    List<MsgLogBean> queryMsgAllByCode(Integer code, Integer otherCode);

	int update4SessionId(WsParamBean bean);

	int update4newPw(WsParamBean userDb);
}
