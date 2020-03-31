package com.cb.pmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cb.pmall.beans.PmsBaseAttrInfo;
import com.cb.pmall.beans.PmsBaseAttrValue;
import com.cb.pmall.service.AttrService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 陈彬
 * date 2020/3/31 2:33 PM
 */
@CrossOrigin
@Controller
public class AttrController {

    @Reference
    AttrService attrService;

    // 查
    @RequestMapping("attrInfoList")
    @ResponseBody
    public List<PmsBaseAttrInfo> attrInfoList(@RequestParam String catalog3Id){
        List<PmsBaseAttrInfo> attrInfos = attrService.attrInfoList(catalog3Id);
        return attrInfos;
    }

    // 增&改
    @RequestMapping("saveAttrInfo")
    @ResponseBody
    public String saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo) {
        String status = attrService.saveAttrInfo(pmsBaseAttrInfo);
        return status;
    }

    //删
    @RequestMapping("deleteAttrInfoById")
    @ResponseBody
    public String deleteAttrInfoById(@RequestParam String attrId) {
        attrService.deleteAttrInfoById(attrId);
        return "success";
    }

    //改之前查出数据
    @RequestMapping("getAttrValueList")
    @ResponseBody
    public List<PmsBaseAttrValue> getAttrValueList(@RequestParam String  attrId) {
        List<PmsBaseAttrValue> attrValueList = attrService.getAttrValueList(attrId);
        return attrValueList;
    }

}
