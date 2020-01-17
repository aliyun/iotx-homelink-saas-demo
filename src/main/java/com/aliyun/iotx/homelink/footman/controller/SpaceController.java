package com.aliyun.iotx.homelink.footman.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.iotx.api.sdk.business.homelink.business.SceneSpaceApi;
import com.aliyun.iotx.api.sdk.business.homelink.business.SpaceApi;
import com.aliyun.iotx.api.sdk.business.homelink.business.SpaceDeviceApi;
import com.aliyun.iotx.api.sdk.business.homelink.business.SpaceUserApi;
import com.aliyun.iotx.api.sdk.business.homelink.dto.device.CommandResult;
import com.aliyun.iotx.api.sdk.business.homelink.dto.device.DeviceBindSpaceResultDTO;
import com.aliyun.iotx.api.sdk.business.homelink.dto.device.DeviceDTO;
import com.aliyun.iotx.api.sdk.business.homelink.dto.scene.SceneDesignDTO;
import com.aliyun.iotx.api.sdk.business.homelink.dto.space.SpaceInfo;
import com.aliyun.iotx.api.sdk.business.homelink.dto.space.SpaceQueryDTO;
import com.aliyun.iotx.api.sdk.business.homelink.dto.space.SpaceUpdateDTO;
import com.aliyun.iotx.api.sdk.dto.IdentityDTO;
import com.aliyun.iotx.api.sdk.dto.PageDTO;
import com.aliyun.iotx.homelink.footman.dto.ResponseDTO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

import static com.aliyun.iotx.homelink.footman.util.Constants.SUCCESS;


@RestController
@RequestMapping(path = "/space")
public class SpaceController extends BaseController {

    @Override
    @PostConstruct
    public void init() {
        super.init();
    }

    /**
     * 创建根空间
     *
     * <pre>
     * {
     *    "operator":{"hid":"wanping.test.1","hidType":"OPEN"},
     *    "spaceInfo":{"name":"测试根节点","description":"测试根空间","countryName":"中国","typeCode":"default"}
     * }
     * </pre>
     *
     * @param request 请求
     * @return 返回
     */
    @PostMapping("/root/create")
    public @ResponseBody
    ResponseDTO<String> createSpace(@RequestBody JSONObject request) {
        ResponseDTO<String> response = new ResponseDTO<>();

        try {
            JSONObject space = request.getJSONObject("spaceInfo");

            SpaceInfo spaceInfo = space.toJavaObject(SpaceInfo.class);

            String data = SpaceApi.createRootSpace(operator, spaceInfo).executeAndGet();

            response.setData(data);
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

    /**
     * 创建子空间
     *
     * <pre>
     * {
     *   "operator":{"hid":"wanping.test.1","hidType":"OPEN"},
     *   "spaceInfo":{
     *      "hid":"wanping.test.1",
     *      "rootSpaceId":"471fece05bfc4087a4a7cf60b06299af",
     *      "hidType":"OPEN",
     *      "name":"次卧卫生间",
     *      "description":"次卧卫生间",
     *      "parentId":"2bd4856a5d164a1abde4a6edf706ea63",
     *      "typeCode":"room"}
     * }
     * </pre>
     *
     * @param request 请求
     * @return 返回
     */
    @PostMapping("/sub/create")
    public @ResponseBody
    ResponseDTO<String> createSubSpace(@RequestBody JSONObject request) {
        ResponseDTO<String> response = new ResponseDTO<>();

        try {
            JSONObject space = request.getJSONObject("spaceInfo");

            SpaceInfo spaceInfo = space.toJavaObject(SpaceInfo.class);

            String data = SpaceApi.createSubSpace(operator, spaceInfo).executeAndGet();

            response.setData(data);
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

    @PostMapping("/delete")
    public @ResponseBody
    ResponseDTO<Integer> delete(@RequestBody JSONObject request) {
        ResponseDTO<Integer> response = new ResponseDTO<>();

        try {
            String spaceId = request.getString("spaceId");

            Integer data = SpaceApi.deleteSpace(operator, spaceId).executeAndGet();

            response.setData(data);
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

    @PostMapping("/root/list")
    public @ResponseBody
    ResponseDTO<PageDTO<SpaceInfo>> getRootSpace(@RequestBody JSONObject request) {
        ResponseDTO<PageDTO<SpaceInfo>> response = new ResponseDTO<>();

        try {
            JSONObject queryJson = request.getJSONObject("query");
            SpaceQueryDTO query = JSON.toJavaObject(queryJson, SpaceQueryDTO.class);

            PageDTO<SpaceInfo> data = SpaceApi.getRootSpace(operator, query).executeAndGet();

            response.setData(data);
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

    @PostMapping("/query")
    public @ResponseBody
    ResponseDTO<List<SpaceInfo>> query(@RequestBody JSONObject request) {
        ResponseDTO<List<SpaceInfo>> response = new ResponseDTO<>();

        try {
            JSONArray list = request.getJSONArray("spaceIds");

            List<SpaceInfo> data = SpaceApi.getSpace(operator, list.toJavaList(String.class)).executeAndGet();

            response.setData(data);
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

    @PostMapping("/sub/get")
    public @ResponseBody
    ResponseDTO<PageDTO<SpaceInfo>> getSubSpace(@RequestBody JSONObject request) {
        ResponseDTO<PageDTO<SpaceInfo>> response = new ResponseDTO<>();

        try {
            JSONObject queryJson = request.getJSONObject("query");
            SpaceQueryDTO query = JSON.toJavaObject(queryJson, SpaceQueryDTO.class);

            PageDTO<SpaceInfo> data = SpaceApi.getSubSpace(operator, query).executeAndGet();

            response.setData(data);
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

    @PostMapping("/update")
    public @ResponseBody
    ResponseDTO<Void> update(@RequestBody JSONObject request) {
        ResponseDTO<Void> response = new ResponseDTO<>();

        try {
            JSONObject data = request.getJSONObject("data");
            SpaceUpdateDTO spaceInfo = JSON.toJavaObject(data, SpaceUpdateDTO.class);

            SpaceApi.updateSpace(operator, spaceInfo).execute();

        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

    @PostMapping("device/bind")
    public @ResponseBody
    ResponseDTO<Void> bindDevice(@RequestBody JSONObject request) {
        ResponseDTO<Void> response = new ResponseDTO<>();

        try {
            String rootSpaceId = request.getString("rootSpaceId");
            String spaceId = request.getString("spaceId");
            String iotId = request.getString("iotId");

            DeviceBindSpaceResultDTO resultDTO = SpaceDeviceApi
                .bindDeviceSpace(operator, rootSpaceId, spaceId, Collections.singletonList(iotId)).executeAndGet();

            if (resultDTO.getSuccessCount() == 0) {
                response.markError("no response");
            } else {
                CommandResult commandResult = resultDTO.getBatchResultMap().get(0).get(iotId);
                if ((commandResult.getCode() != SUCCESS.getCode())) {
                    response.markError(commandResult.getMessage());
                }
            }
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

    @PostMapping("/device/unbind")
    public @ResponseBody
    ResponseDTO<Void> unbindDevice(@RequestBody JSONObject request) {
        ResponseDTO<Void> response = new ResponseDTO<>();

        try {
            String rootSpaceId = request.getString("rootSpaceId");
            String spaceId = request.getString("spaceId");
            String iotId = request.getString("iotId");

            DeviceBindSpaceResultDTO resultDTO = SpaceDeviceApi
                .unbindDeviceSpace(operator, rootSpaceId, spaceId, Collections.singletonList(iotId)).executeAndGet();

            if (resultDTO.getSuccessCount() == 0) {
                response.markError("no response");
            } else {
                CommandResult commandResult = resultDTO.getBatchResultMap().get(0).get(iotId);
                if ((commandResult.getCode() != SUCCESS.getCode())) {
                    response.markError(commandResult.getMessage());
                }
            }
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

    @PostMapping("/device/list")
    public @ResponseBody
    ResponseDTO<PageDTO<DeviceDTO>> listDevice(@RequestBody JSONObject request) {
        ResponseDTO<PageDTO<DeviceDTO>> response = new ResponseDTO<>();

        try {
            String rootSpaceId = request.getString("rootSpaceId");
            String spaceId = request.getString("spaceId");
            boolean includeSubSpace = request.getBoolean("includeSubSpace");
            int pageNo = request.getInteger("pageNo");
            int pageSize = request.getInteger("pageSize");

            PageDTO<DeviceDTO> pageDTO = SpaceDeviceApi
                .querySpaceDevices(operator, rootSpaceId, spaceId, includeSubSpace, pageNo, pageSize).executeAndGet();

            response.setData(pageDTO);
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
            JSONObject targetJson = request.getJSONObject("target");
            IdentityDTO target = targetJson.toJavaObject(IdentityDTO.class);
            String spaceId = request.getString("spaceId");

            SpaceUserApi.bindUserSpace(operator, Collections.singletonList(target), spaceId).execute();
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
            String spaceId = request.getString("spaceId");

            SpaceUserApi.unbindUserSpace(operator, target, spaceId).execute();
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

    @PostMapping("/user/list")
    public @ResponseBody
    ResponseDTO<PageDTO<IdentityDTO>> listUser(@RequestBody JSONObject request) {
        ResponseDTO<PageDTO<IdentityDTO>> response = new ResponseDTO<>();

        try {
            String spaceId = request.getString("spaceId");
            int pageNo = request.getInteger("pageNo");
            int pageSize = request.getInteger("pageSize");

            PageDTO<IdentityDTO> data = SpaceUserApi.querySpaceUsers(operator, spaceId, pageNo, pageSize)
                .executeAndGet();

            response.setData(data);
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

    @PostMapping("/scene/bind")
    public @ResponseBody
    ResponseDTO<Void> bindScene(@RequestBody JSONObject request) {
        ResponseDTO<Void> response = new ResponseDTO<>();

        try {
            String spaceId = request.getString("spaceId");
            String sceneId = request.getString("sceneId");

            SceneSpaceApi.bindSpaceScene(operator, spaceId, Collections.singletonList(sceneId)).execute();
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

    @PostMapping("scene/unbind")
    public @ResponseBody
    ResponseDTO<Void> unbindScene(@RequestBody JSONObject request) {
        ResponseDTO<Void> response = new ResponseDTO<>();

        try {
            String spaceId = request.getString("spaceId");
            String sceneId = request.getString("sceneId");

            SceneSpaceApi.unbindSpaceScene(operator, spaceId, Collections.singletonList(sceneId)).execute();
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

    @PostMapping("/scene/list")
    public @ResponseBody
    ResponseDTO<PageDTO<SceneDesignDTO>> listScene(@RequestBody JSONObject request) {
        ResponseDTO<PageDTO<SceneDesignDTO>> response = new ResponseDTO<>();

        try {
            String spaceId = request.getString("spaceId");
            int pageNo = request.getInteger("pageNo");
            int pageSize = request.getInteger("pageSize");

            PageDTO<SceneDesignDTO> data = SceneSpaceApi
                .querySpaceScenes(operator, spaceId, null, pageNo, pageSize).executeAndGet();

            response.setData(data);
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

    @PostMapping("/scene/clear")
    public @ResponseBody
    ResponseDTO<Void> unbindAllScene(@RequestBody JSONObject request) {
        ResponseDTO<Void> response = new ResponseDTO<>();

        try {
            String spaceId = request.getString("spaceId");
            SceneSpaceApi.unbindSpaceAllScene(operator, spaceId).execute();
        } catch (Exception e) {
            response.markError(e.getMessage());
        }

        return response;
    }

}
