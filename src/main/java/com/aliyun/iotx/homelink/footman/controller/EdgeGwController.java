package com.aliyun.iotx.homelink.footman.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.aliyun.iotx.api.sdk.business.homelink.business.EdgeGatewayApi;
import com.aliyun.iotx.api.sdk.dto.IdentityDTO;
import com.aliyun.iotx.api.sdk.dto.PageDTO;
import com.aliyun.iotx.homelink.footman.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

import static java.util.Objects.isNull;


@RestController
@RequestMapping("/edge/gw")
@Slf4j
public class EdgeGwController extends BaseController {

    public static final TypeReference<List<String>>
        RETURN_TYPE_STRING_LIST = new TypeReference<List<String>>() {};

    @Override
    @PostConstruct
    public void init() {
        super.init();
    }

    /**
     * 查询空间的边缘网关
     *
     * @return 网关列表
     */
    @PostMapping("/space/gateway/query")
    @ResponseBody
    ResponseDTO<PageDTO<String>> querySpaceGateway(@RequestBody JSONObject request) {
        return executor(() -> EdgeGatewayApi.querySpaceGateway(
            operator,
            request.getString("spaceId"),
            request.getInteger("pageNo"),
            request.getInteger("pageSize")
        ).executeAndGet());
    }

    /**
     * 查询人的边缘网关
     *
     * @return 网关列表
     */
    @PostMapping("/user/gateway/query")
    @ResponseBody
    ResponseDTO<PageDTO<String>> queryUserGateway(@RequestBody JSONObject request) {
        JSONObject targetJson = request.getJSONObject("target");
        if (isNull(targetJson)) {
            ResponseDTO<PageDTO<String>> resp = new ResponseDTO<>();
            resp.markError("没有target参数");
            return resp;
        }
        IdentityDTO target = targetJson.toJavaObject(IdentityDTO.class);
        if (isNull(target)) {
            ResponseDTO<PageDTO<String>> resp = new ResponseDTO<>();
            resp.markError("无效的target参数");
            return resp;
        }

        return executor(() -> EdgeGatewayApi.queryUserGateway(
            operator,
            target,
            request.getInteger("pageNo"),
            request.getInteger("pageSize")
        ).executeAndGet());
    }

    /**
     * 查询租户的边缘网关
     *
     * @return 网关列表
     */
    @PostMapping("/gateway/query")
    @ResponseBody
    ResponseDTO<PageDTO<String>> queryGateway(@RequestBody JSONObject request) {
        return executor(() -> EdgeGatewayApi.queryGateway(
            operator,
            request.getInteger("pageNo"),
            request.getInteger("pageSize")
        ).executeAndGet());
    }

    /**
     * 推送配置到边缘网关设备
     */
    @PostMapping("/config/push")
    @ResponseBody
    ResponseDTO<Void> pushConfig(@RequestBody JSONObject request) {
        return executorVoid(() -> EdgeGatewayApi.pushConfig(
            operator,
            request.getString("edgeIotId")
        ).executeAndGet());
    }

    /**
     * 清空边缘网关上的配置
     */
    @PostMapping("/config/clear")
    @ResponseBody
    ResponseDTO<Void> clearConfig(@RequestBody JSONObject request) {
        return executorVoid(() -> EdgeGatewayApi.clearConfig(
            operator,
            request.getString("edgeIotId")
        ).executeAndGet());
    }

    /**
     * 移除边缘网关上的配置
     */
    @PostMapping("/config/delete")
    @ResponseBody
    ResponseDTO<Void> deleteConfig(@RequestBody JSONObject request) {
        return executorVoid(() -> EdgeGatewayApi.deleteConfig(
            operator,
            request.getString("edgeIotId")
        ).executeAndGet());
    }

    /**
     * 向边缘网关配置中添加设备
     */
    @PostMapping("/subdevice/add")
    @ResponseBody
    ResponseDTO<Void> addDeviceToGateway(@RequestBody JSONObject request) {
        JSONArray iotIdArray = request.getJSONArray("iotIds");
        if (isNull(iotIdArray)) {
            ResponseDTO<Void> resp = new ResponseDTO<>();
            resp.markError("iotIds");
            return resp;
        }
        List<String> iotIds = iotIdArray.toJavaObject(RETURN_TYPE_STRING_LIST);
        if (isNull(iotIds)) {
            ResponseDTO<Void> resp = new ResponseDTO<>();
            resp.markError("无效的iotIds参数");
            return resp;
        }

        return executorVoid(() -> EdgeGatewayApi.addDeviceToGateway(
            operator,
            iotIds,
            request.getString("edgeIotId")
        ).executeAndGet());
    }

    /**
     * 从边缘网关配置中删除设备
     */
    @PostMapping("/subdevice/delete")
    @ResponseBody
    ResponseDTO<Void> deleteDeviceFromGateway(@RequestBody JSONObject request) {
        JSONArray iotIdArray = request.getJSONArray("iotIds");
        if (isNull(iotIdArray)) {
            ResponseDTO<Void> resp = new ResponseDTO<>();
            resp.markError("iotIds");
            return resp;
        }
        List<String> iotIds = iotIdArray.toJavaObject(RETURN_TYPE_STRING_LIST);
        if (isNull(iotIds)) {
            ResponseDTO<Void> resp = new ResponseDTO<>();
            resp.markError("无效的iotIds参数");
            return resp;
        }

        return executorVoid(() -> EdgeGatewayApi.deleteDeviceFromGateway(
            operator,
            iotIds,
            request.getString("edgeIotId")
        ).executeAndGet());
    }

    /**
     * 查询已经加入到边缘网关配置下的子设备列表
     *
     * @return 网关列表
     */
    @PostMapping("/subdevice/query")
    @ResponseBody
    ResponseDTO<PageDTO<String>> queryDeviceInGateway(@RequestBody JSONObject request) {
        return executor(() -> EdgeGatewayApi.queryDeviceInGateway(
            operator,
            request.getString("edgeIotId"),
            request.getInteger("pageNo"),
            request.getInteger("pageSize")
        ).executeAndGet());
    }

    /**
     * 查询边缘网关已连接但未加入到配置下的子设备列表
     *
     * @return 网关列表
     */
    @PostMapping("/subdevice/unconfigured/query")
    @ResponseBody
    ResponseDTO<PageDTO<String>> queryUnConfiguredDeviceInGateway(@RequestBody JSONObject request) {
        return executor(() -> EdgeGatewayApi.queryUnConfiguredDeviceInGateway(
            operator,
            request.getString("edgeIotId"),
            request.getInteger("pageNo"),
            request.getInteger("pageSize")
        ).executeAndGet());
    }

    /**
     * 向边缘网关配置中添加场景
     */
    @PostMapping("/scene/add")
    @ResponseBody
    ResponseDTO<Void> addSceneToGateway(@RequestBody JSONObject request) {
        return executorVoid(() -> EdgeGatewayApi.addSceneToGateway(
            operator,
            request.getString("edgeIotId"),
            request.getString("sceneId")
        ).executeAndGet());
    }

    /**
     * 从边缘网关配置中删除场景
     */
    @PostMapping("/scene/delete")
    @ResponseBody
    ResponseDTO<Void> deleteSceneFromGateway(@RequestBody JSONObject request) {
        return executorVoid(() -> EdgeGatewayApi.deleteSceneFromGateway(
            operator,
            request.getString("edgeIotId"),
            request.getString("sceneId")
        ).executeAndGet());
    }

    /**
     * 查询边缘网关配置中的场景
     *
     * @return 网关列表
     */
    @PostMapping("/scene/query")
    @ResponseBody
    ResponseDTO<PageDTO<String>> querySceneInGateway(@RequestBody JSONObject request) {
        return executor(() -> EdgeGatewayApi.querySceneInGateway(
            operator,
            request.getString("edgeIotId"),
            request.getInteger("pageNo"),
            request.getInteger("pageSize")
        ).executeAndGet());
    }

}
