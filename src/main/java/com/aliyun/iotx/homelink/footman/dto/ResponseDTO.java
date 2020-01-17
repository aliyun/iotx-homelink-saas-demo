package com.aliyun.iotx.homelink.footman.dto;

import lombok.Data;

import java.io.Serializable;


@Data
public class ResponseDTO<T> implements Serializable {

    private static final long serialVersionUID = -4606627101534347611L;

    private Integer code;

    private String message;

    private T data;

    public ResponseDTO() {
        code = 200;
        message = "OK";
    }

    public void markError(String errorMsg) {
        code = 500;
        message = errorMsg;
    }
}
