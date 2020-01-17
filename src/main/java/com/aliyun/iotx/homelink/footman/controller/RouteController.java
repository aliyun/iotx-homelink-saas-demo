package com.aliyun.iotx.homelink.footman.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class RouteController {

    @GetMapping("/scene/list")
    public String sceneRunList() {
        return "/index.html";
    }

    @GetMapping("/scene/setting")
    public String sceneSetting() {
        return "/index.html";
    }

    @GetMapping("/scene/detail")
    public String sceneDetail() {
        return "/index.html";
    }

    @GetMapping("/device/list")
    public String deviceList() {
        return "/index.html";
    }

    @GetMapping("/device/detail")
    public String deviceDetail() {
        return "/index.html";
    }

    @GetMapping("/space/list")
    public String spaceList() {
        return "/index.html";
    }

    @GetMapping("/space/detail")
    public String spaceDetail() {
        return "/index.html";
    }

    @GetMapping("/user/detail")
    public String userDetail() {
        return "/index.html";
    }

    @GetMapping("/user/list")
    public String userList() {
        return "/index.html";
    }

    @GetMapping("/category")
    public String category() {
        return "/index.html";
    }

    @GetMapping("/edgeGateway/list")
    public String edgeGatewayList() {
        return "/index.html";
    }

    @GetMapping("/edgeGateway/detail")
    public String edgeGatewayDetail() {
        return "/index.html";
    }

    @GetMapping("/thelog")
    public String log() {
        return "/index.html";
    }
}
