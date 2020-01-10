package com.wchat.dao;

import com.wchat.bean.MsgLogBean;
import com.wchat.bean.WsParamBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {

    int insertUser(@Param("bean") WsParamBean bean);

    List<WsParamBean> queryUserByUserName(@Param("userName") String userName);

    String getUserPw(@Param("code") Integer code);

    int update4Login(@Param("bean") WsParamBean bean);

    WsParamBean getUserByCode(@Param("code") Integer code);

    int update4Offline(@Param("bean") WsParamBean bean);

    int insertMsgLog(@Param("bean") MsgLogBean msgLogBean);

    void update4SystemInit();

    List<WsParamBean> queryUserByStatus(@Param("status") Integer status,@Param("code") Integer code);

    List<MsgLogBean> queryMsgAllByCode(@Param("code") Integer code,@Param("otherCode") Integer otherCode);

	int update4SessionId(@Param("bean") WsParamBean bean);

	int update4newPw(@Param("bean") WsParamBean userDb);
}
