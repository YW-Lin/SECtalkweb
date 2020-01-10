package com.wchat.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wchat.bean.MsgLogBean;
import com.wchat.bean.ResultBean;
import com.wchat.bean.VerifiCodeCacheBean;
import com.wchat.bean.WsParamBean;
import com.wchat.service.UserService;
import com.wchat.socket.WebSocketServer;
import com.wchat.utils.SecurityUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/ws")
@Log
public class WebSocketController extends BaseController{

    @Autowired
    UserService userService;

    @RequestMapping(value="/sendAll", method= RequestMethod.POST)
    public ResultBean sendAllMessage(WsParamBean bean){
//        try {
//            WebSocketServer.BroadCastInfo(message);
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
        return super.getSuccResult("ok");
    }
    
    @RequestMapping(value="/sendOne", method=RequestMethod.POST)
    public ResultBean sendOneMessage(WsParamBean bean){
        ResultBean result = super.getResult();
        result.setCode(failCode);
        if(StringUtils.isEmpty(bean.getToken())){
            result.setMsg("token is empty!");
            return result;
        }
        if(!userService.checkToken(bean.getToken())){
            return super.getResult(invalid,"token is invalid!");
        }
        if(StringUtils.isEmpty(bean.getOtherCode())){
            result.setMsg("otherCode is empty!");
            return result;
        }
        if(StringUtils.isEmpty(bean.getMsg())){
            result.setMsg("message is empty!");
            return result;
        }
        WsParamBean sendUser = userService.getUserByToken(bean.getToken());
        WsParamBean acceptUser = userService.getUserByCode(bean.getOtherCode());
        if(acceptUser==null){
            result.setMsg("other user do not exist!");
            return result;
        }
        if(acceptUser.getSessionId()==null){
            result.setMsg("other user is offline!");
            return result;
        }
        MsgLogBean msgLogBean = new MsgLogBean(sendUser.getCode(), sendUser.getUserName(), acceptUser.getCode(), acceptUser.getUserName(), bean.getMsg());

        try {
            WebSocketServer.SendMessage(msgLogBean.toString(),acceptUser.getSessionId());
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("message send fail:"+e.getMessage());
            return result;
        }
        userService.insertMsgLog(msgLogBean);
        return super.getSuccResult("ok");
    }

    @RequestMapping(value="/getLineList", method=RequestMethod.POST)
    public String getLineList(WsParamBean bean) throws Exception {
        ResultBean result = super.getResult();
        if(StringUtils.isEmpty(bean.getToken())){
            result.setMsg("token is empty!");
            return SecurityUtil.encryptMsg(result.toString());
        }
        if(!userService.checkToken(bean.getToken())){
            ResultBean re = super.getResult(invalid, "token is invalid!");
            return SecurityUtil.encryptMsg(re.toString());
        }
        WsParamBean userDb = userService.getUserByToken(bean.getToken());
        List<WsParamBean> list = userService.queryUserByStatus(1,userDb.getCode());

        return SecurityUtil.encryptMsg(JSON.toJSONString(super.getSuccResult(list)));

    }

    @RequestMapping(value="/registerUser", method=RequestMethod.POST)
    public ResultBean registerUser(WsParamBean bean){
        ResultBean result = super.getResult();
        result.setCode(failCode);
        if(StringUtils.isEmpty(bean.getUserName())){
            result.setMsg("the userName is empty!");
            return result;
        }
        if(StringUtils.isEmpty(bean.getEmail())){
            result.setMsg("the E-mail is empty!");
            return result;
        }
        if(StringUtils.isEmpty(bean.getPw())){
            result.setMsg("the password is empty!");
            return result;
        }
        if(bean.getUserName().length()>10){
            result.setMsg("the userName is too long!");
            return result;
        }
        if(!Pattern.matches("^[A-Za-z0-9]+$",bean.getUserName())){
            result.setMsg("Must be at least 8 characters, contain capital letters!");
            return result;
        }
        if(bean.getPw().length()<8){
            result.setMsg("Must be at least 8 characters, contain capital letters!");
            return result;
        }
        char[] chars = bean.getPw().toCharArray();
        boolean lower = false;
        boolean capital = false;
        for (char c : chars) {
            if(c>=97&&c<=122)   lower = true;
            if(c>=65&&c<=90)   capital = true;
        }
        if(!lower){
            result.setMsg("Password must contain lower letter!");
            return result;
        }
        if(!capital){
            result.setMsg("Password must contain capital letter!");
            return result;
        }
        if(userService.checkUserName(bean.getUserName())){
            result.setMsg("the userName is exist!");
            return result;
        }
        if(StringUtils.isEmpty(bean.getUuid())){
            result.setMsg("the uuid is empty!");
            return result;
        }
        if(StringUtils.isEmpty(bean.getVerifiCode())){
            result.setMsg("the verifiCode is empty!");
            return result;
        }
        Map<String, String> map = VerifiCodeCacheBean.getCache().getMap();
        if(!map.containsKey(bean.getUuid())||!map.get(bean.getUuid()).equals(bean.getVerifiCode())){
            result.setMsg("verifiCode error!");
            return result;
        }
        map.remove(bean.getUuid());
        bean.setPw(SecurityUtil.encryptPw(bean.getPw()));
        if(bean.getPw()==null||"".equals(bean.getPw())){
            result.setMsg("register fail!");
            return result;
        }
        int i = userService.insertUser(bean);
        if(i==1){
            return super.getSuccResult("register sucess!");
        }
        return super.getFailResult("register fail!");
    }

    @RequestMapping(value="/userLogin", method=RequestMethod.POST)
    public ResultBean userLogin(WsParamBean bean) throws Exception {
        ResultBean result = super.getResult();
        result.setCode(failCode);
        if(StringUtils.isEmpty(bean.getUserName())){
            result.setMsg("the userName is empty!");
            return result;
        }
        if(StringUtils.isEmpty(bean.getPw())){
            result.setMsg("the password is empty!");
            return result;
        }
        if(StringUtils.isEmpty(bean.getUuid())){
            result.setMsg("the uuid is empty!");
            return result;
        }
        if(StringUtils.isEmpty(bean.getVerifiCode())){
            result.setMsg("the verifiCode is empty!");
            return result;
        }
        Map<String, String> map = VerifiCodeCacheBean.getCache().getMap();
        if(!map.containsKey(bean.getUuid())||!map.get(bean.getUuid()).equals(bean.getVerifiCode().toLowerCase())){
            result.setMsg("verifiCode error!");
            return result;
        }
        map.remove(bean.getUuid());
        List<WsParamBean> list = userService.queryUserByUserName(bean.getUserName());
        if(list==null||list.isEmpty()){
            result.setMsg("This account does not exist!");
            return result;
        }
        WsParamBean db = list.get(0);
        bean.setCode(list.get(0).getCode());
        if(!userService.checkPw(bean)){
            result.setMsg("Wrong userName or password!");
            return result;
        }
        JSONObject jo = SecurityUtil.buildToken(bean.getCode());
        bean.setUuid(jo.getString("uuid"));
        if(userService.update4Login(bean)!=1){
            result.setMsg("login fail!");
            return result;
        }
        if(db.getStatus()==1){
			WsParamBean msg = new WsParamBean(null, WsParamBean.msgType_sys);
			msg.setCode(310);
			WebSocketServer.SendMessage(msg.toString(),db.getSessionId());
        }
        return super.getSuccResult4Data(jo.getString("token"));
    }

    @RequestMapping(value="/autoLogin", method=RequestMethod.POST)
    public ResultBean autoLogin(WsParamBean bean){
        ResultBean result = super.getResult();
        result.setCode(failCode);
        if(StringUtils.isEmpty(bean.getToken())){
            result.setMsg("token is empty!");
            return result;
        }
        if(!userService.checkToken(bean.getToken())){
            return super.getResult(invalid,"token is invalid!");
        }
//        WsParamBean userDb = userService.getUserByToken(bean.getToken());
//        JSONObject jo = SecurityUtil.buildToken(userDb.getCode());
//        bean.setUuid(jo.getString("uuid"));
//        bean.setCode(userDb.getCode());
//        if(userService.update4Login(bean)!=1){
//            return super.getResult(invalid,"token is invalid!");
//        }
        return super.getSuccResult4Data("ok");
    }

    @RequestMapping(value="/offline", method=RequestMethod.POST)
    public ResultBean offline(WsParamBean bean){
        ResultBean result = super.getResult();
        result.setCode(failCode);
        if(StringUtils.isEmpty(bean.getToken())){
            result.setMsg("token is empty!");
            return result;
        }
        if(!userService.checkToken(bean.getToken())){
            return super.getResult(invalid,"token is invalid!");
        }
        WsParamBean userDb = userService.getUserByToken(bean.getToken());
        if(userService.update4Offline(userDb)!=1){
            result.setMsg("offline fail!");
            return result;
        }
        return super.getSuccResult4Data("ok");
    }

    @RequestMapping(value="/getMsgList", method=RequestMethod.POST)
    public String getMsgList(WsParamBean bean) throws Exception {
        ResultBean result = super.getResult();
        result.setCode(failCode);
        if(StringUtils.isEmpty(bean.getToken())){
            result.setMsg("token is empty!");
            return SecurityUtil.encryptMsg(result.toString());
        }
        if(!userService.checkToken(bean.getToken())){
            ResultBean re = super.getResult(invalid, "token is invalid!");
            return SecurityUtil.encryptMsg(re.toString());
        }
        if(StringUtils.isEmpty(bean.getOtherCode())){
            result.setMsg("otherCode is empty!");
            return SecurityUtil.encryptMsg(result.toString());
        }
        WsParamBean userDb = userService.getUserByToken(bean.getToken());
        PageHelper.startPage(bean.getPageNo(),bean.getPageSize());
        List<MsgLogBean> list = userService.queryMsgAllByCode(userDb.getCode(), bean.getOtherCode());
        PageInfo<MsgLogBean> page = new PageInfo<MsgLogBean>(list);
        return SecurityUtil.encryptMsg(JSON.toJSONString(super.getSuccResult(page)));
    }


    @RequestMapping(value="/update4Session", method=RequestMethod.POST)
    public ResultBean update4Session(WsParamBean bean) throws Exception {
        ResultBean result = super.getResult();
        result.setCode(failCode);
        if(StringUtils.isEmpty(bean.getToken())){
            result.setMsg("token is empty!");
            return result;
        }
        if(StringUtils.isEmpty(bean.getSessionId())){
            result.setMsg("sessionId is empty!");
            return result;
        }
        if(!userService.checkToken(bean.getToken())){
            return super.getResult(invalid,"token is invalid!");
        }
        WsParamBean userDb = userService.getUserByToken(bean.getToken());
        userDb.setSessionId(bean.getSessionId());
        if(userService.update4SessionId(userDb)==1){
            return super.getSuccResult("ok");
        }
        return super.getFailResult("update fail");
    }

    @RequestMapping(value="/getEmailByUserName", method=RequestMethod.POST)
    public ResultBean getEmailByUserName(WsParamBean bean) {
        ResultBean result = super.getResult();
        result.setCode(failCode);
        if(StringUtils.isEmpty(bean.getUserName())){
            result.setMsg("userName is empty!");
            return result;
        }
        List<WsParamBean> db = userService.queryUserByUserName(bean.getUserName());
        if(db==null||db.size()==0){
            result.setMsg("The user was not found!");
            return result;
        }
        return super.getSuccResult4Data(db.get(0).getEmail());
    }


    @RequestMapping(value="/update4password", method=RequestMethod.POST)
    public ResultBean update4password(WsParamBean bean) {
        ResultBean result = super.getResult();
        result.setCode(failCode);
        if(StringUtils.isEmpty(bean.getToken())){
            result.setMsg("The token is empty!");
            return result;
        }
        if(StringUtils.isEmpty(bean.getPw())){
            result.setMsg("The original password is empty!");
            return result;
        }
        if(StringUtils.isEmpty(bean.getNewPw())){
            result.setMsg("The new password is empty!");
            return result;
        }
        if(StringUtils.isEmpty(bean.getUuid())){
            result.setMsg("the uuid is empty!");
            return result;
        }
        if(StringUtils.isEmpty(bean.getVerifiCode())){
            result.setMsg("the verifiCode is empty!");
            return result;
        }
        if(bean.getNewPw().length()<8){
            result.setMsg("Must be at least 8 characters, contain capital letters!");
            return result;
        }
        char[] chars = bean.getNewPw().toCharArray();
        boolean lower = false;
        boolean capital = false;
        for (char c : chars) {
            if(c>=97&&c<=122)   lower = true;
            if(c>=65&&c<=90)   capital = true;
        }
        if(!lower){
            result.setMsg("Password must contain lower letter!");
            return result;
        }
        if(!capital){
            result.setMsg("Password must contain capital letter!");
            return result;
        }
        Map<String, String> map = VerifiCodeCacheBean.getCache().getMap();
        if(!map.containsKey(bean.getUuid())||!map.get(bean.getUuid()).equals(bean.getVerifiCode())){
            result.setMsg("verifiCode error!");
            return result;
        }
        map.remove(bean.getUuid());
        if(!userService.checkToken(bean.getToken())){
            return super.getResult(invalid,"token is invalid!");
        }
        WsParamBean userDb = userService.getUserByToken(bean.getToken());
        userDb.setPw(bean.getPw());
        if(!userService.checkPw(userDb)){
            result.setMsg("original wrong password!");
            return result;
        }
        userDb.setPw(SecurityUtil.encryptPw(bean.getNewPw()));
        if(userDb.getPw()==null||"".equals(userDb.getPw())){
            result.setMsg("fail to edit!");
            return result;
        }
        if(userService.update4newPw(userDb)==1){
            return super.getSuccResult("success to edit");
        }
        return super.getFailResult("fail to edit!");
    }

}