package com.cb.pmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cb.pmall.beans.PmsProductImage;
import com.cb.pmall.beans.PmsProductInfo;
import com.cb.pmall.beans.PmsProductSaleAttr;
import com.cb.pmall.manage.utils.UploadUtils;
import com.cb.pmall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 陈彬
 * date 2020/4/1 4:42 PM
 */
@Controller
@CrossOrigin
public class SpuController {

    @Reference
    SpuService spuService;

    @RequestMapping("spuList")
    @ResponseBody
    public List<PmsProductInfo> spuList(@RequestParam String catalog3Id) {
        List<PmsProductInfo> productInfos = spuService.spuList(catalog3Id);
        return productInfos;
    }


    @RequestMapping("saveSpuInfo")
    @ResponseBody
    public String saveSpuInfo(@RequestBody PmsProductInfo pmsProductInfo) {
        spuService.saveSpuInfo(pmsProductInfo);
        return "success";
    }

    @RequestMapping("delSpuById")
    @ResponseBody
    public String delSpuById(String spuId) {
        spuService.delSpuById(spuId);
        return "success";
    }

    @RequestMapping("fileUpload")
    @ResponseBody
    public String fileUpload(@RequestParam("file")MultipartFile multipartFile) {
        String imgUrl = UploadUtils.uploadFile(multipartFile);
        return imgUrl;
    }

    @ResponseBody
    @RequestMapping("spuSaleAttrList")
    public List<PmsProductSaleAttr> spuSaleAttrList(@RequestParam String spuId) {
        List<PmsProductSaleAttr> pmsProductSaleAttrs = spuService.spuSaleAttrList(spuId);
        return pmsProductSaleAttrs;
    }

    @ResponseBody
    @RequestMapping("spuImageList")
    public List<PmsProductImage> spuImageList(@RequestParam String spuId) {
        List<PmsProductImage> images = spuService.spuImageList(spuId);
        return images;
    }

}
