package com.cb.pmall.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.cb.pmall.beans.*;
import com.cb.pmall.service.SkuService;
import com.cb.pmall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 陈彬
 * date 2020/4/3 8:20 AM
 */
@Controller
@CrossOrigin
public class itemController {

    @Reference
    SpuService spuService;

    @Reference
    SkuService skuService;

    @RequestMapping("{skuId}.html")
    public String item(@PathVariable String skuId, ModelMap map, HttpServletRequest request) {
        // 获取IP地址
        String ip = request.getRemoteAddr();
        // 获取sku
        PmsSkuInfo skuInfo = skuService.getSkuById(skuId,ip);
        // 将sku加入model
        map.put("skuInfo", skuInfo);
        // 根据spuId和skuId获取相同spuId的销售属性和销售属性值,用于页面展示
        List<PmsProductSaleAttr> pmsProductSaleAttrs = spuService.spuSaleAttrListCheckBySku(skuInfo.getSpuId(), skuInfo.getId());
        // 将属性值与属性加入modelMap
        map.put("spuSaleAttrListCheckBySku", pmsProductSaleAttrs);

        // 获取skuImage并存入
        List<PmsSkuImage>images = skuService.getSkuImageBySkuId(skuId);
        skuInfo.setSkuImageList(images);
        // 制作saleAttrHashMap
        Map<String, String> SaleAttrMap = new HashMap<>();
        String key;
        String value = "";
        // 重新获取所有sku并存入hashMap
        List<PmsSkuInfo> pmsSkuInfos = skuService.getSkuSaleAttrValueListBySpu(skuInfo.getSpuId());
        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {
            key = "";
            // 获取skuId作为值
            value = pmsSkuInfo.getId();
            List<PmsSkuSaleAttrValue> skuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList();
            //  获取循环获取销售属性集合
            for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
                // 创建 key
                key += pmsSkuSaleAttrValue.getSaleAttrValueId() + "|";
            }
            SaleAttrMap.put(key, value);
        }
        // 将hashMap转为Json
        String skuSaleAttrHashJsonStr = JSON.toJSONString(SaleAttrMap);
        map.put("skuSaleAttrHashJsonStr",skuSaleAttrHashJsonStr);


        return "item";
    }

}