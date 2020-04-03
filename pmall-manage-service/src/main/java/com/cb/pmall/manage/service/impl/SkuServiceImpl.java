package com.cb.pmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.cb.pmall.beans.*;
import com.cb.pmall.manage.mapper.*;
import com.cb.pmall.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author 陈彬
 * date 2020/4/2 4:39 PM
 */
@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;

    @Autowired
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;

    @Autowired
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;

    @Autowired
    PmsSkuImageMapper pmsSkuImageMapper;


    @Override
    public String saveSkuInfo(PmsSkuInfo pmsSkuInfo) {

        pmsSkuInfoMapper.insertSelective(pmsSkuInfo);
        String SkuId = pmsSkuInfo.getId();
        // 保存平台属性
        List<PmsSkuAttrValue> pmsSkuAttrValueList = pmsSkuInfo.getSkuAttrValueList();
        for (PmsSkuAttrValue pmsSkuAttrValue : pmsSkuAttrValueList) {
            pmsSkuAttrValue.setSkuId(SkuId);
            pmsSkuAttrValueMapper.insertSelective(pmsSkuAttrValue);
        }

        // 保存销售属性
        List<PmsSkuSaleAttrValue> pmsSkuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList();
        for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : pmsSkuSaleAttrValueList) {
            pmsSkuSaleAttrValue.setSkuId(SkuId);
            pmsSkuSaleAttrValueMapper.insert(pmsSkuSaleAttrValue);
        }

        // 保存图片
        List<PmsSkuImage> pmsSkuImageList = pmsSkuInfo.getSkuImageList();
        for (PmsSkuImage pmsSkuImage : pmsSkuImageList) {
            pmsSkuImage.setSkuId(SkuId);
            pmsSkuImageMapper.insertSelective(pmsSkuImage);
        }
        return "success";
    }
}
