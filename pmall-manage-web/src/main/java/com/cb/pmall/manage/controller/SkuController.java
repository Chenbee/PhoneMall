package com.cb.pmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cb.pmall.beans.*;
import com.cb.pmall.service.SkuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author 陈彬
 * date 2020/4/2 4:30 PM
 */
@CrossOrigin
@Controller
public class SkuController {

    @Reference
    SkuService skuService;

    @RequestMapping
    @ResponseBody
    public String saveSkuInfo(@RequestBody PmsSkuInfo pmsSkuInfo){

        String success = skuService.saveSkuInfo(pmsSkuInfo);

        return success;
    }

}
