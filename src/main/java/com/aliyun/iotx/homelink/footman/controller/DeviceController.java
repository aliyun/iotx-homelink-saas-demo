package com.aliyun.iotx.homelink.footman.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.iotx.api.sdk.business.homelink.business.DeviceApi;
import com.aliyun.iotx.api.sdk.business.homelink.business.DeviceUserApi;
import com.aliyun.iotx.api.sdk.business.homelink.dto.device.*;
import com.aliyun.iotx.api.sdk.business.homelink.dto.user.DeviceUserRoleEnum;
import com.aliyun.iotx.api.sdk.dto.IdentityDTO;
import com.aliyun.iotx.api.sdk.dto.IdentityRoleDTO;
import com.aliyun.iotx.api.sdk.dto.PageDTO;
import com.aliyun.iotx.api.sdk.dto.PageSearchDTO;
import com.aliyun.iotx.api.util.entity.IoTxResult;
import com.aliyun.iotx.homelink.footman.dto.ResponseDTO;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("/device")
public class DeviceController extends BaseController {

    @Override
    @PostConstruct
    public void init() {
        super.init();
    }

    @PostMapping("/tsl/get")
    @ResponseBody
    ResponseDTO<JSONObject> getDeviceTslDefinition(@RequestBody JSONObject request) {
        ResponseDTO<JSONObject> response = new ResponseDTO<>();

        try {
            String iotId = request.getString("iotId");

            JSONObject data = DeviceApi.getDeviceTsl(operator, iotId).executeAndGet();
            response.setData(data);
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

    @PostMapping("/bindinfo/get")
    @ResponseBody
    ResponseDTO<List<SpaceDeviceDTO>> getDevice(@RequestBody JSONObject request) {
        ResponseDTO<List<SpaceDeviceDTO>> response = new ResponseDTO<>();

        try {
            JSONArray iotIds = request.getJSONArray("iotIds");

            List<SpaceDeviceDTO> data = DeviceApi.getDevice(operator, iotIds.toJavaList(String.class))
                .apiVer("1.0.1")
                .executeAndGet();
            response.setData(data);
        } catch (Exception e) {
            response.markError(e.getMessage());
        }

        return response;
    }

    @PostMapping("/control")
    @ResponseBody
    ResponseDTO<DeviceControlResultDTO> controlDevice(@RequestBody JSONObject request) {
        ResponseDTO<DeviceControlResultDTO> response = new ResponseDTO<>();

        try {
            String iotId = request.getString("iotId");
            JSONArray commands = request.getJSONArray("commands");
            List<CommandTypeDTO> list = commands.toJavaList(CommandTypeDTO.class);

            DeviceControlResultDTO data = DeviceApi.controlDevice(operator, iotId, list).executeAndGet();
            response.setData(data);
        } catch (Exception e) {
            response.markError(e.getMessage());
        }

        return response;
    }

    @PostMapping("/query")
    @ResponseBody
    ResponseDTO<PageDTO<DeviceDTO>> queryDevice(@RequestBody JSONObject request) {
        ResponseDTO<PageDTO<DeviceDTO>> response = new ResponseDTO<>();

        try {
            int pageSize = request.getInteger("pageSize");
            int pageNo = request.getInteger("pageNo");

            PageDTO<DeviceDTO> data = DeviceApi.queryDevices(operator, pageNo, pageSize).executeAndGet();
            response.setData(data);
        } catch (Exception e) {
            response.markError(e.getMessage());
        }

        return response;
    }

    @PostMapping("/id/get")
    @ResponseBody
    ResponseDTO<DeviceBaseDTO> getIotId(@RequestBody JSONObject request) {
        ResponseDTO<DeviceBaseDTO> response = new ResponseDTO<>();

        try {
            String productKey = request.getString("productKey");
            String deviceName = request.getString("deviceName");

            DeviceBaseDTO data = DeviceApi.getIotId(operator, productKey, deviceName).executeAndGet();
            response.setData(data);
        } catch (Exception e) {
            response.markError(e.getMessage());
        }

        return response;
    }

    @PostMapping("/subDevice")
    @ResponseBody
    ResponseDTO<PageDTO<DeviceBaseDTO>> querySubDevice(@RequestBody JSONObject request) {
        ResponseDTO<PageDTO<DeviceBaseDTO>> response = new ResponseDTO<>();

        try {
            String iotId = request.getString("iotId");
            int pageSize = request.getInteger("pageSize");
            int pageNo = request.getInteger("pageNo");

            PageDTO<DeviceBaseDTO> data = DeviceApi.queryGatewaySubDevices(operator, iotId, pageNo, pageSize)
                .executeAndGet();
            response.setData(data);
        } catch (Exception e) {
            response.markError(e.getMessage());
        }

        return response;
    }

    @PostMapping("/gateway")
    @ResponseBody
    ResponseDTO<GatewayDeviceDTO> getGateway(@RequestBody JSONObject request) {
        ResponseDTO<GatewayDeviceDTO> response = new ResponseDTO<>();

        try {
            String iotId = request.getString("iotId");

            GatewayDeviceDTO data = DeviceApi.queryGatewayOfSubDevice(operator, iotId).executeAndGet();
            response.setData(data);
        } catch (Exception e) {
            response.markError(e.getMessage());
        }

        return response;
    }

    @PostMapping("/reset")
    @ResponseBody
    ResponseDTO<DeviceResetResultDTO> reset(@RequestBody JSONObject request) {
        ResponseDTO<DeviceResetResultDTO> response = new ResponseDTO<>();

        try {
            String iotId = request.getString("iotId");

            DeviceResetResultDTO data = DeviceApi.resetDevice(operator, iotId).executeAndGet();
            response.setData(data);
        } catch (Exception e) {
            response.markError(e.getMessage());
        }

        return response;
    }

    @PostMapping("/nickname/update")
    @ResponseBody
    ResponseDTO<Void> updateNickName(@RequestBody JSONObject request) {
        ResponseDTO<Void> response = new ResponseDTO<>();

        try {
            String iotId = request.getString("iotId");
            String nickName = request.getString("nickName");

            DeviceApi.updateDeviceNickName(operator, iotId, nickName).execute();

        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

    @PostMapping("/user/list")
    @ResponseBody
    ResponseDTO<PageDTO<IdentityRoleDTO>> listUser(@RequestBody JSONObject request) {
        ResponseDTO<PageDTO<IdentityRoleDTO>> response = new ResponseDTO<>();

        try {
            String iotId = request.getString("iotId");
            Integer pageNo = request.getInteger("pageNo");
            Integer pageSize = request.getInteger("pageSize");
            PageSearchDTO queryDTO = new PageSearchDTO();
            queryDTO.setPageNo(pageNo);
            queryDTO.setPageSize(pageSize);

            PageDTO<IdentityRoleDTO> data = DeviceUserApi
                .queryDeviceUsersWithRole(operator, iotId, queryDTO).executeAndGet();
            response.setData(data);
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

    @PostMapping("/user/bind")
    @ResponseBody
    ResponseDTO<Void> bindUser(@RequestBody JSONObject request) {
        ResponseDTO<Void> response = new ResponseDTO<>();

        try {
            String iotId = request.getString("iotId");

            JSONObject targetJson = request.getJSONObject("target");
            IdentityDTO target = targetJson.toJavaObject(IdentityDTO.class);

            List<UserResultDTO> resultList = DeviceUserApi
                .bindUserDevice(operator, iotId, Collections.singletonList(target)).executeAndGet();

            if (CollectionUtils.isEmpty(resultList)) {
                response.markError("no response");
            } else {
                IoTxResult result = resultList.get(0).getResult();
                if (!result.hasSucceeded()) {
                    response.markError(result.getMessage());
                }
            }
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

    @PostMapping("/user/unbind")
    @ResponseBody
    ResponseDTO<Void> unbindUser(@RequestBody JSONObject request) {
        ResponseDTO<Void> response = new ResponseDTO<>();

        try {
            String iotId = request.getString("iotId");

            JSONObject targetJson = request.getJSONObject("target");
            IdentityDTO target = targetJson.toJavaObject(IdentityDTO.class);

            List<UserResultDTO> resultList = DeviceUserApi
                .unbindUserDevice(operator, iotId, Collections.singletonList(target)).executeAndGet();

            if (CollectionUtils.isEmpty(resultList)) {
                response.markError("no response");
            }
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

    @PostMapping("/user/role/set")
    @ResponseBody
    ResponseDTO<List<UserResultDTO>> setDeviceUserRole(@RequestBody JSONObject request) {
        ResponseDTO<List<UserResultDTO>> response = new ResponseDTO<>();

        try {
            String iotId = request.getString("iotId");
            JSONObject targetJson = request.getJSONObject("target");
            IdentityDTO target = targetJson.toJavaObject(IdentityDTO.class);
            String roleCode = request.getString("roleCode");
            DeviceUserRoleEnum deviceUserRoleEnum = DeviceUserRoleEnum.valueOf(roleCode);

            List<UserResultDTO> resultList = DeviceUserApi.setDeviceUserRole(
                operator, iotId, Collections.singletonList(target), deviceUserRoleEnum).executeAndGet();

            if (CollectionUtils.isEmpty(resultList)) {
                response.markError("no response");
            }
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

    @PostMapping("/lp/bind")
    @ResponseBody
    ResponseDTO<String> bindLPDevice(@RequestBody JSONObject request) {
        ResponseDTO<String> response = new ResponseDTO<>();

        try {
            String iotId = request.getString("iotId");
            String productKey = request.getString("productKey");
            String deviceName = request.getString("deviceName");

            JSONObject targetJson = request.getJSONObject("target");
            IdentityDTO target = targetJson.toJavaObject(IdentityDTO.class);

            SingleIotIdDTO singleIotIdDTO = DeviceUserApi
                .bindLpDevice(operator, iotId, productKey, deviceName, target).executeAndGet();

            response.setData(singleIotIdDTO.getIotId());
        } catch (Exception e) {
            response.markError(e.getMessage());
        }
        return response;
    }

}
