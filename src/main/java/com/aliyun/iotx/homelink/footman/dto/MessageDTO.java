package com.aliyun.iotx.homelink.footman.dto;

import lombok.Data;

import java.io.Serializable;


@Data
public class MessageDTO implements Serializable {

    private static final long serialVersionUID = -3278865647209584799L;

    private String topic;

    private String payload;

    public MessageDTO(String topic, String payload) {
        this.topic = topic;
        this.payload = payload;
    }
}
