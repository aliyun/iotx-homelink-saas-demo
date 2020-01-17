package com.aliyun.iotx.homelink.footman.controller;

import com.aliyun.iotx.api.sdk.dto.IdentityDTO;
import com.aliyun.iotx.api.util.context.ApiContext;
import com.aliyun.iotx.homelink.footman.dto.ResponseDTO;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Value;

import java.util.function.Supplier;

import static java.lang.Boolean.parseBoolean;


public class BaseController {

    protected IdentityDTO operator;

    @Value("${footman.api.appKey}")
    private String appKey;

    @Value("${footman.api.appSecret}")
    private String appSecret;

    @Value("${footman.api.hid}")
    private String hid;

    @Value("${footman.api.hidType}")
    private String hidType;

    @Value("${footman.api.host}")
    private String apiHost;

    @Value("${footman.api.system}")
    private String system;

    public void init() {
        ApiContext.setEnv("default", apiHost, appKey, appSecret, 3000L, true);

        if (!Strings.isNullOrEmpty(system) && "false".equalsIgnoreCase(system)) {
            ApiContext.setPre(!parseBoolean(system));
        }

        operator = new IdentityDTO();
        operator.setHid(hid);
        operator.setHidType(hidType);
    }

    static <T> ResponseDTO<T> executor(Supplier<T> supplier) {
        ResponseDTO<T> response = new ResponseDTO<>();
        try {
            T data = supplier.get();
            response.setData(data);
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

    static ResponseDTO<Void> executorVoid(Supplier<?> supplier) {
        ResponseDTO<Void> response = new ResponseDTO<>();
        try {
            supplier.get();
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }
}
