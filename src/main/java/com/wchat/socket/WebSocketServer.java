package com.wchat.socket;

import com.wchat.bean.WsParamBean;
import com.wchat.utils.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint(value = "/ws/asset")
@Component
public class WebSocketServer {

    @PostConstruct
    public void init() {
        System.out.println("websocket init");
    }
    private static Logger log = LoggerFactory.getLogger(WebSocketServer.class);
    private static final AtomicInteger OnlineCount = new AtomicInteger(0);
    public static CopyOnWriteArraySet<Session> SessionSet = new CopyOnWriteArraySet<Session>();


    @OnOpen
    public void onOpen(Session session) {
        SessionSet.add(session);
        int cnt = OnlineCount.incrementAndGet();
        log.info("There are connections to join, the current number of connections is:{}", cnt);
        WsParamBean bean = new WsParamBean(session.getId(), WsParamBean.msgType_sys);
        SendMessage(session,bean.toString());
    }

    @OnClose
    public void onClose(Session session) {
        SessionSet.remove(session);
        int cnt = OnlineCount.decrementAndGet();
        log.info("There is a connection closed, the current number of connections is:{}", cnt);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("Message from the client:{}",message);

    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("An error occurred:{},Session ID: {}",error.getMessage(),session.getId());
//        error.printStackTrace();
    }

    public static void SendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(SecurityUtil.encryptMsg(message));
        } catch (Exception e) {
            log.error("Error sending a message:{}", e.getMessage());
            e.printStackTrace();
        }
    }

    public static void BroadCastInfo(String message) throws IOException {
        for (Session session : SessionSet) {
            if(session.isOpen()){
//                SendMessage(session, message);
            }
        }
    }

    public static void SendMessage(String message,String sessionId) throws Exception {
        Session session = null;
        for (Session s : SessionSet) {
            if(s.getId().equals(sessionId)){
                session = s;
                break;
            }
        }
        if(session!=null){
            SendMessage(session, message);
        }
        else{
            log.warn("Did not find the session with your specified ID:{}",sessionId);
            throw new Exception("accept user is offline");
        }
    }

}
