package com.wchat.controller;

import com.wchat.bean.ResultBean;
import com.wchat.bean.VerifiCodeCacheBean;
import com.wchat.service.UserService;
import com.wchat.socket.WebSocketServer;
import com.wchat.utils.ImageVerificationCode;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

@RequestMapping("/api/common")
@RestController
@Log
public class CommonController extends BaseController{

    @Autowired
    UserService userService;

    @RequestMapping("/getVerifiCode")
    @ResponseBody
    public ResultBean getVerifiCode(HttpServletResponse response,String uuid) throws IOException {
        ResultBean result = super.getResult();
        result.setCode(failCode);
        if(uuid==null||uuid.equals("")){
            result.setMsg("uuid is empty");
            return result;
        }
        Map<String, String> map = VerifiCodeCacheBean.getCache().getMap();
        if(map.containsKey(uuid)){
            result.setMsg("uuid has been repeated");
            return result;
        }

        ImageVerificationCode ivc = new ImageVerificationCode();
        BufferedImage image = ivc.getImage();
        map.put(uuid,ivc.getText().toLowerCase());
        ivc.output(image, response.getOutputStream());
        return null;
    }

    @RequestMapping("/getSessions")
    @ResponseBody
    public ResultBean getSessions(){
        CopyOnWriteArraySet<Session> sessionSet = WebSocketServer.SessionSet;
        ArrayList<String> list = new ArrayList<>();
        for (Session session : sessionSet) {
            list.add(session.getId());
        }

        return super.getSuccResult(list);
    }

}
