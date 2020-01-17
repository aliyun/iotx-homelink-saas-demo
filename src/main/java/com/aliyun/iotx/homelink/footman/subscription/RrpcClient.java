package com.aliyun.iotx.homelink.footman.subscription;

import com.aliyun.iotx.homelink.footman.dto.MessageDTO;
import com.aliyun.openservices.iot.api.Profile;
import com.aliyun.openservices.iot.api.message.MessageClientFactory;
import com.aliyun.openservices.iot.api.message.api.MessageClient;
import com.aliyun.openservices.iot.api.message.callback.MessageCallback;
import com.aliyun.openservices.iot.api.message.entity.Message;
import com.aliyun.openservices.iot.api.message.entity.MessageToken;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;


@Component
@Data
public class RrpcClient {

    @Resource
    private MessageService messageService;

    @Value("${footman.accessKey}")
    private String accessKey;

    @Value("${footman.accessSecret}")
    private String accessSecret;

    @Value("${footman.aliyunUid}")
    private String aliyunUid;

    @Value("${footman.regionId}")
    private String regionId;

    private String endPoint;

    public void start(String aliyunUid, String accessKey, String accessSecret) {

        endPoint = "https://" + aliyunUid + ".iot-as-http2." + regionId + ".aliyuncs.com";

        // 连接配置
        Profile profile = Profile.getAccessKeyProfile(endPoint, regionId, accessKey, accessSecret);

        // 构造客户端
        MessageClient client = MessageClientFactory.messageClient(profile);

        client.connect(messageToken -> {
            Message m = messageToken.getMessage();

            System.out.println("\n\ntopic=" + m.getTopic());
            System.out.println("payload=" + new String(m.getPayload()));
            System.out.println("generateTime=" + m.getGenerateTime());

            sendMessage(messageToken);

            // 此处标记CommitSuccess已消费，IoT平台会删除当前Message，否则会保留到过期时间
            return MessageCallback.Action.CommitSuccess;
        });

        // 设备重置 - 添加指定topic监听
        //String resetTopic = "/broadcast/" + aliyunUid + "/homelink/device";
        //client.setMessageListener(resetTopic, this::sendMessage);

        // 场景执行 - 添加指定topic监听
        //String sceneRunTopic = "/sys/uid/" + aliyunUid + "/homelink/scene/result";
        //client.subscribe(sceneRunTopic, this::sendMessage);
    }

    @PostConstruct
    void init() {
        start(aliyunUid, accessKey, accessSecret);
    }

    private MessageCallback.Action sendMessage(MessageToken messageToken) {
        Message m = messageToken.getMessage();
        String msg = new String(m.getPayload());

        MessageDTO messageDTO = new MessageDTO(m.getTopic(), msg);
        messageService.sendMessage(messageDTO.toString());

        return MessageCallback.Action.CommitSuccess;
    }

}
