package com.aliyun.iotx.homelink.footman.subscription;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentSkipListSet;


/**
 * 用于推送IoT的上行事件消息
 */
@Service
@ServerEndpoint("/{sid}")
@Slf4j
@Data
public class MessageService implements Comparable {

    private static ConcurrentSkipListSet<MessageService> websocketSet;

    private Session session;

    private String sid = "";

    static {
        websocketSet = new ConcurrentSkipListSet<>();
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        this.session = session;

        websocketSet.add(this);

        this.sid = sid;
        try {
            sendMessage("连接成功 " + sid);
        } catch (Exception e) {
            log.error("websocket IO异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        websocketSet.remove(this);
    }

    public void sendMessage(String message) {
        websocketSet.forEach(p -> p.getSession().getAsyncRemote().sendText(message));
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return this.hashCode() - o.hashCode();
    }
}
