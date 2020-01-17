package com.aliyun.iotx.homelink.footman.controller;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.iotx.api.sdk.business.homelink.business.DeviceUserApi;
import com.aliyun.iotx.api.sdk.business.homelink.business.SceneUserApi;
import com.aliyun.iotx.api.sdk.business.homelink.business.SpaceUserApi;
import com.aliyun.iotx.api.sdk.business.homelink.dto.device.DeviceDTO;
import com.aliyun.iotx.api.sdk.business.homelink.dto.scene.SceneDesignDTO;
import com.aliyun.iotx.api.sdk.business.homelink.dto.user.ThirdAccountListDTO;
import com.aliyun.iotx.api.sdk.dto.IdentityDTO;
import com.aliyun.iotx.api.sdk.dto.PageDTO;
import com.aliyun.iotx.api.sdk.platform.thirdparty.ThirdPartyApi;
import com.aliyun.iotx.homelink.footman.dto.ResponseDTO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;


@RestController
@RequestMapping(path = "/user")
public class UserController extends BaseController {

    @Override
    @PostConstruct
    public void init() {
        super.init();
    }

    @PostMapping("/space/query")
    public @ResponseBody
    ResponseDTO<PageDTO<String>> getSpace(@RequestBody JSONObject request) {
        ResponseDTO<PageDTO<String>> response = new ResponseDTO<>();

        try {
            JSONObject targetJson = request.getJSONObject("target");
            IdentityDTO target = targetJson.toJavaObject(IdentityDTO.class);

            Integer pageSize = request.getInteger("pageSize");
            Integer pageNo = request.getInteger("pageNo");

            PageDTO<String> data = SpaceUserApi.queryUserSpaces(operator, target, pageNo, pageSize).executeAndGet();

            response.setData(data);
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

    @PostMapping("/device/query")
    public @ResponseBody
    ResponseDTO<PageDTO<DeviceDTO>> getDevice(@RequestBody JSONObject request) {
        ResponseDTO<PageDTO<DeviceDTO>> response = new ResponseDTO<>();

        try {
            JSONObject targetJson = request.getJSONObject("target");
            IdentityDTO target = targetJson.toJavaObject(IdentityDTO.class);

            Integer pageSize = request.getInteger("pageSize");
            Integer pageNo = request.getInteger("pageNo");

            PageDTO<DeviceDTO> data = DeviceUserApi.queryUserDevices(operator, target, pageNo, pageSize)
                .executeAndGet();

            response.setData(data);
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

    @PostMapping("/scene/query")
    public @ResponseBody
    ResponseDTO<PageDTO<SceneDesignDTO>> getScene(@RequestBody JSONObject request) {
        ResponseDTO<PageDTO<SceneDesignDTO>> response = new ResponseDTO<>();

        try {
            JSONObject targetJson = request.getJSONObject("target");
            IdentityDTO target = targetJson.toJavaObject(IdentityDTO.class);

            Integer pageSize = request.getInteger("pageSize");
            Integer pageNo = request.getInteger("pageNo");

            PageDTO<SceneDesignDTO> data = SceneUserApi.queryUserScenes(operator, target, pageNo, pageSize)
                .executeAndGet();

            response.setData(data);
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

    @PostMapping("/import")
    public @ResponseBody
    ResponseDTO<String> importUser(@RequestBody JSONObject request) {
        ResponseDTO<String> response = new ResponseDTO<>();

        try {
            String openId = request.getString("openId");
            String nickName = request.getString("nickName");
            String phone = request.getString("phone");
            String email = request.getString("email");
            String avatarUrl = request.getString("avatarUrl");
            String creator = request.getString("creater");
            String loginName = request.getString("loginName");

            String result = ThirdPartyApi.importAccountForThirdParty(
                openId, nickName, phone, email, avatarUrl, creator, loginName, null, null, null)
                .executeAndGet();

            response.setData(result);
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

    @PostMapping("/list")
    public @ResponseBody
    ResponseDTO<ThirdAccountListDTO> listUser(@RequestBody JSONObject request) {
        ResponseDTO<ThirdAccountListDTO> response = new ResponseDTO<>();

        try {
            Integer pageNo = request.getInteger("pageNo");
            Integer pageSize = request.getInteger("pageSize");

            ThirdAccountListDTO thirdAccountListDTO = ThirdPartyApi
                .queryThirdAccountList(pageNo, pageSize).executeAndGet();

            response.setData(thirdAccountListDTO);
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

    @PostMapping("/identity/get")
    public @ResponseBody
    ResponseDTO<com.aliyun.iotx.api.sdk.business.homelink.dto.user.IdentityDTO> getIdentityId(
        @RequestBody JSONObject request) {
        ResponseDTO<com.aliyun.iotx.api.sdk.business.homelink.dto.user.IdentityDTO> response = new ResponseDTO<>();

        try {
            String openId = request.getString("openId");

            com.aliyun.iotx.api.sdk.business.homelink.dto.user.IdentityDTO identityDTO = ThirdPartyApi
                .getAccountByOpenId(openId).executeAndGet();

            response.setData(identityDTO);
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

}
