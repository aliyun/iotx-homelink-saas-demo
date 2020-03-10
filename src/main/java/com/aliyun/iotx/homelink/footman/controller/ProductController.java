package com.aliyun.iotx.homelink.footman.controller;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.iotx.api.sdk.business.homelink.business.ProductApi;
import com.aliyun.iotx.api.sdk.business.homelink.dto.device.CategoryDTO;
import com.aliyun.iotx.api.sdk.business.homelink.dto.product.ProductDTO;
import com.aliyun.iotx.api.sdk.dto.PageDTO;
import com.aliyun.iotx.homelink.footman.dto.ResponseDTO;
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;



@RestController
@RequestMapping("/product")
public class ProductController extends BaseController {

    @Override
    @PostConstruct
    public void init() {
        super.init();
    }

    /**
     * 用于分页搜索产品，可以按指定pk，或者指定品类，或者指定
     *
     * @return 产品信息
     */
    @PostMapping("/query")
    @ResponseBody
    ResponseDTO<PageDTO<ProductDTO>> query(@RequestBody JSONObject request) {
        ResponseDTO<PageDTO<ProductDTO>> response = new ResponseDTO<>();

        try {
            String productKey = request.getString("productKey");
            String productName = request.getString("productName");
            String categoryKey = request.getString("categoryKey");
            Integer pageNo = request.getInteger("pageNo");
            Integer pageSize = request.getInteger("pageSize");

            PageDTO<ProductDTO> data = ProductApi
                .queryProducts(operator, productKey, productName, categoryKey, pageNo, pageSize).executeAndGet();
            response.setData(data);
        } catch (Exception e) {
            response.markError(e.getMessage());
        }

        return response;
    }

    /**
     * 分页搜索品类
     *
     * @return 产品信息
     */
    @PostMapping("/category/query")
    @ResponseBody
    ResponseDTO<PageDTO<CategoryDTO>> queryCategory(@RequestBody JSONObject request) {
        ResponseDTO<PageDTO<CategoryDTO>> response = new ResponseDTO<>();

        try {
            String categoryName = request.getString("categoryName");
            Long superId = request.getLong("superId");
            //Integer pageNo = request.getInteger("pageNo");
            Integer pageSize = request.getInteger("pageSize");

            int pageNo = 1;

            List<CategoryDTO> all = Lists.newLinkedList();
            while (true) {
                PageDTO<CategoryDTO> page = ProductApi
                    .queryCategories(operator, categoryName, superId, pageNo, pageSize).executeAndGet();

                List<CategoryDTO> data = page.getData();
                if (CollectionUtils.isEmpty(data)) {
                    break;
                }
                all.addAll(data);

                if (all.size() >= page.getTotal()) {
                    break;
                }
                pageNo++;
            }

            PageDTO<CategoryDTO> result = new PageDTO<>();
            result.setData(all);
            result.setPageNo(1);
            result.setPageSize(all.size());
            result.setTotal(all.size());

            response.setData(result);
        } catch (Exception e) {
            response.markError(e.getMessage());
        }

        return response;
    }

    /**
     * 获取产品TSL
     *
     * @return 产品TSL
     */
    @PostMapping("/tsl/get")
    @ResponseBody
    ResponseDTO<JSONObject> getTsl(@RequestBody JSONObject request) {
        ResponseDTO<JSONObject> response = new ResponseDTO<>();

        try {
            String productKey = request.getString("productKey");

            JSONObject data = ProductApi.getProductTsl(productKey, null).executeAndGet();
            response.setData(data);
        } catch (Exception e) {
            response.markError(e.getMessage());
        }

        return response;
    }

}
