package com.aliyun.iotx.homelink.footman.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.iotx.api.sdk.business.homelink.business.SceneApi;
import com.aliyun.iotx.api.sdk.business.homelink.business.SceneDeviceApi;
import com.aliyun.iotx.api.sdk.business.homelink.business.SceneUserApi;
import com.aliyun.iotx.api.sdk.business.homelink.dto.device.DeviceTcaDTO;
import com.aliyun.iotx.api.sdk.business.homelink.dto.scene.SceneDesignDTO;
import com.aliyun.iotx.api.sdk.dto.IdentityDTO;
import com.aliyun.iotx.api.sdk.dto.PageDTO;
import com.aliyun.iotx.homelink.footman.dto.ResponseDTO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;


@RestController
@RequestMapping(path = "/scene")
public class SceneController extends BaseController {

    @Override
    @PostConstruct
    public void init() {
        super.init();
    }

    @PostMapping("/create")
    public @ResponseBody
    ResponseDTO<String> createScene(@RequestBody JSONObject request) {
        ResponseDTO<String> response = new ResponseDTO<>();

        try {
            String name = request.getString("name");
            String icon = request.getString("icon");
            String description = request.getString("description");
            JSONArray triggers = request.getJSONArray("triggers");
            JSONArray conditions = request.getJSONArray("conditions");
            JSONArray actions = request.getJSONArray("actions");

            String data = SceneApi.createScene(operator, name, icon, description, triggers,
                conditions, actions, true).executeAndGet();
            response.setData(data);
        } catch (Exception e) {
            response.markError(e.getMessage());
        }

        return response;
    }

    @PostMapping("/delete")
    public @ResponseBody
    ResponseDTO<Void> deleteScene(@RequestBody JSONObject request) {

        ResponseDTO<Void> response = new ResponseDTO<>();
        try {
            String sceneId = request.getString("sceneId");

            SceneApi.deleteScene(operator, sceneId).execute();
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

    @PostMapping("/update/base")
    public @ResponseBody
    ResponseDTO<Void> updateSceneBase(@RequestBody JSONObject request) {
        ResponseDTO<Void> response = new ResponseDTO<>();
        try {
            String sceneId = request.getString("sceneId");
            String name = request.getString("name");
            String description = request.getString("description");
            String icon = request.getString("icon");

            SceneApi.updateSceneBase(operator, name, icon, description, sceneId).execute();
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

    @PostMapping("/update/tca")
    public @ResponseBody
    ResponseDTO<Void> updateSceneTca(@RequestBody JSONObject request) {
        ResponseDTO<Void> response = new ResponseDTO<>();
        try {
            String sceneId = request.getString("sceneId");
            JSONArray triggers = request.getJSONArray("triggers");
            JSONArray conditions = request.getJSONArray("conditions");
            JSONArray actions = request.getJSONArray("actions");

            SceneApi.updateSceneTca(operator, sceneId, triggers, conditions, actions).execute();
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

    @PostMapping("/switch")
    public @ResponseBody
    ResponseDTO<Void> switchScene(@RequestBody JSONObject request) {
        ResponseDTO<Void> response = new ResponseDTO<>();
        try {
            String sceneId = request.getString("sceneId");
            Boolean enable = request.getBoolean("enable");

            SceneApi.updateSceneSwitch(operator, enable, sceneId).execute();
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

    @PostMapping(path = "/list")
    public @ResponseBody
    ResponseDTO<PageDTO<SceneDesignDTO>> queryScene(@RequestBody JSONObject request) {
        ResponseDTO<PageDTO<SceneDesignDTO>> response = new ResponseDTO<>();
        try {
            Integer pageSize = request.getInteger("pageSize");
            Integer pageNo = request.getInteger("pageNo");

            PageDTO<SceneDesignDTO> data = SceneApi.queryScene(operator, pageNo, pageSize).executeAndGet();
            response.setData(data);
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

    @PostMapping("/info")
    public @ResponseBody
    ResponseDTO<SceneDesignDTO> getSceneInfo(@RequestBody JSONObject request) {
        ResponseDTO<SceneDesignDTO> response = new ResponseDTO<>();
        try {
            String sceneId = request.getString("sceneId");

            SceneDesignDTO data = SceneApi.getScene(operator, sceneId).executeAndGet();
            response.setData(data);
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

    @PostMapping("/run")
    public @ResponseBody
    ResponseDTO<Void> runScene(@RequestBody JSONObject request) {
        ResponseDTO<Void> response = new ResponseDTO<>();
        try {
            String sceneId = request.getString("sceneId");

            SceneApi.runScene(operator, sceneId).execute();
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

    @PostMapping("/device/tenant/tca/list")
    public @ResponseBody
    ResponseDTO<PageDTO<DeviceTcaDTO>> getTcaDevices(@RequestBody JSONObject request) {
        ResponseDTO<PageDTO<DeviceTcaDTO>> response = new ResponseDTO<>();
        try {
            Integer pageNo = request.getInteger("pageNo");
            Integer pageSize = request.getInteger("pageSize");
            Integer flowType = request.getInteger("flowType");
            String name = request.getString("name");

            PageDTO<DeviceTcaDTO> data = SceneDeviceApi.querySceneDevices(operator, pageNo, pageSize, flowType, name)
                .executeAndGet();
            response.setData(data);
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

    @PostMapping("/user/bind")
    public @ResponseBody
    ResponseDTO<Void> bindUser(@RequestBody JSONObject request) {
        ResponseDTO<Void> response = new ResponseDTO<>();
        try {
            String sceneId = request.getString("sceneId");

            JSONObject targetJson = request.getJSONObject("target");
            IdentityDTO target = targetJson.toJavaObject(IdentityDTO.class);

            SceneUserApi.bindUserScene(operator, sceneId, target).execute();
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

    @PostMapping("/user/unbind")
    public @ResponseBody
    ResponseDTO<Void> unbindUser(@RequestBody JSONObject request) {
        ResponseDTO<Void> response = new ResponseDTO<>();
        try {
            JSONObject targetJson = request.getJSONObject("target");
            IdentityDTO target = targetJson.toJavaObject(IdentityDTO.class);

            String sceneId = request.getString("sceneId");

            SceneUserApi.unbindUserScene(operator, target, sceneId).execute();
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

    @PostMapping("/user/list")
    public @ResponseBody
    ResponseDTO<List<IdentityDTO>> getUserList(@RequestBody JSONObject request) {
        ResponseDTO<List<IdentityDTO>> response = new ResponseDTO<>();
        try {
            String sceneId = request.getString("sceneId");

            List<IdentityDTO> data = SceneUserApi.querySceneUsers(operator, sceneId).executeAndGet();

            response.setData(data);
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

}
