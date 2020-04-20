package com.cb.pmall.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cb.pmall.beans.*;
import com.cb.pmall.service.AttrService;
import com.cb.pmall.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

/**
 * @author 陈彬
 * date 2020/4/19 3:02 PM
 */
@Controller
@CrossOrigin
public class SearchController {

    @Reference
    SearchService searchService;

    @Reference
    AttrService attrService;

    @RequestMapping("list.html")
    public String list(PmsSearchParam pmsSearchParam, ModelMap map) {

        List<PmsSearchSkuInfo> pmsSearchSkuInfoList = searchService.searchList(pmsSearchParam);
        // 将skuLsInfoList传回浏览器
        map.put("skuLsInfoList", pmsSearchSkuInfoList);

        // 创建set集合用于存放valueId
        Set<String> valueIdSet = new HashSet<>();
        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfoList) {
            List<PmsSkuAttrValue> skuAttrValueList = pmsSearchSkuInfo.getSkuAttrValueList();
            for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
                // 将所有valueId存入set集合
                String valueId = pmsSkuAttrValue.getValueId();
                valueIdSet.add(valueId);
            }
        }

        // 通过set集合里的value检索出attrInfoList
        List<PmsBaseAttrInfo> attrInfoList = attrService.getAttrInfoByValueId(valueIdSet);

        // 在attr传入前端时,先删除searchParam里的attr
        String[] valueIds = pmsSearchParam.getValueId();
        // 利用迭代器删除AttoInfo
        Iterator<PmsBaseAttrInfo> iterator = attrInfoList.iterator();
        List<PmsSearchCrumb> attrValueSelectedList = new ArrayList();
        while (iterator.hasNext()) {
            PmsBaseAttrInfo pmsBaseAttrInfo = iterator.next();
            List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
            if (attrInfoList != null) {
                // attrinfoList不为空则去除已查询的属性
                for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                    if (valueIds != null) {
                        // valueIds不为空,开始去除查询参数
                        for (String valueId : valueIds) {
                            if (pmsBaseAttrValue.getId().equals(valueId)) {
                                // 去除attrInfo里的查询条件的valueId,并将其制作成面包屑
                                // 制作面包屑
                                PmsSearchCrumb pmsSearchCrumb = new PmsSearchCrumb();
                                // 设置valueId与valueName
                                pmsSearchCrumb.setValueId(valueId);
                                pmsSearchCrumb.setValueName(pmsBaseAttrValue.getValueName());
                                // 设置urlParam
                                String urlParamForCrumb = getUrlParam(pmsSearchParam, valueId);
                                pmsSearchCrumb.setUrlParam(urlParamForCrumb);
                                attrValueSelectedList.add(pmsSearchCrumb);
                                iterator.remove();
                            }
                        }
                    }
                }
            }
        }
        // 将attrInfoList传回浏览器
        map.put("attrList", attrInfoList);


        // 将urlParam传入到map
        String urlParam = getUrlParam(pmsSearchParam, null);
        map.put("urlParam", urlParam);

        // 前端展示需要keyword
        String keyword = pmsSearchParam.getKeyword();
        map.put("keyword", keyword);

        // 将面包屑传入map
        map.put("attrValueSelectedList", attrValueSelectedList);

        return "list";
    }

    @RequestMapping("index")
    public String list() {
        return "index";
    }

    public String getUrlParam(PmsSearchParam pmsSearchParam, String valueId) {
        // 参数获取
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        String keyword = pmsSearchParam.getKeyword();
        String[] valueIds = pmsSearchParam.getValueId();
        String urlParam = "";

        if (StringUtils.isNotBlank(catalog3Id)) {
            urlParam += "&catalog3Id=" + catalog3Id;
        }

        if (StringUtils.isNotBlank(keyword)) {
            urlParam += "&keyword=" + keyword;
        }

        if (valueIds != null) {
            if (valueId == null) {
                // valueid为空,拼接attr的urlParam
                for (String id : valueIds) {
                    urlParam += "&valueId=" + id;
                }
            } else {
                //valueId不为空,拼接面包屑的urlParam
                for (String id : valueIds) {
                    if (!id.equals(valueId))
                        urlParam += "&valueId=" + id;
                }
            }

        }
        // 去除第一个&
        urlParam = urlParam.substring(1, urlParam.length());


        return urlParam;
    }

}
