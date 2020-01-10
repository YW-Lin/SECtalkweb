package com.wchat.service.impl;

import com.wchat.bean.MsgLogBean;
import com.wchat.bean.TokenBean;
import com.wchat.bean.WsParamBean;
import com.wchat.dao.UserDao;
import com.wchat.service.UserService;
import com.wchat.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public int insertUser(WsParamBean bean) {
        return userDao.insertUser(bean);
    }

    @Override
    public boolean checkUserName(String userName) {
        List<WsParamBean> list = this.queryUserByUserName(userName);
        if(list==null||list.isEmpty())  return false;
        return true;
    }

    @Override
    public List<WsParamBean> queryUserByUserName(String userName) {
        return userDao.queryUserByUserName(userName);
    }

    @Override
    public boolean checkPw(WsParamBean bean) {
        String dbPw = userDao.getUserPw(bean.getCode());
        String encryptPw = SecurityUtil.encryptPw(bean.getPw());
        if(!dbPw.equals(encryptPw)) return false;
        return true;
    }

    @Override
    public int update4Login(WsParamBean bean) {
        return userDao.update4Login(bean);
    }

    @Override
    public boolean checkToken(String token) {
        TokenBean tokenBean = SecurityUtil.decryptToken(token);
        if(!tokenBean.isComplete()) return false;
        WsParamBean userDb = getUserByCode(tokenBean.getCode());
        if(tokenBean.verifyToken4User(userDb))  return true;
        return false;
    }
    @Override
    public WsParamBean getUserByCode(Integer code) {
        return userDao.getUserByCode(code);
    }

    @Override
    public WsParamBean getUserByToken(String token) {
        TokenBean tokenBean = SecurityUtil.decryptToken(token);
        if(!tokenBean.isComplete()) return null;
        return getUserByCode(tokenBean.getCode());
    }

    @Override
    public int update4Offline(WsParamBean bean) {
        return userDao.update4Offline(bean);
    }

    @Override
    public int insertMsgLog(MsgLogBean msgLogBean) {
        return userDao.insertMsgLog(msgLogBean);
    }

    @Override
    public void update4SystemInit() {
        userDao.update4SystemInit();
    }

    @Override
    public List<WsParamBean> queryUserByStatus(Integer status, Integer code) {
        return userDao.queryUserByStatus(status,code);
    }

    @Override
    public List<MsgLogBean> queryMsgAllByCode(Integer code, Integer otherCode) {
        return userDao.queryMsgAllByCode(code,otherCode);
    }

    @Override
    public int update4SessionId(WsParamBean bean) {
        return userDao.update4SessionId(bean);
    }

    @Override
    public int update4newPw(WsParamBean userDb) {
        return userDao.update4newPw(userDb);
    }
}
