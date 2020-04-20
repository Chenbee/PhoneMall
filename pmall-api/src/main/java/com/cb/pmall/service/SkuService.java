package com.cb.pmall.service;

import com.cb.pmall.beans.*;

import java.util.List;

/**
 * @author 陈彬
 * date 2020/4/2 4:38 PM
 */
public interface SkuService {

    String saveSkuInfo(PmsSkuInfo pmsSkuInfo);

    PmsSkuInfo getSkuById(String skuId,String ip);

    List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(String spuId);

    List<PmsSkuImage> getSkuImageBySkuId(String skuId);

    List<PmsSkuInfo> getAllSku();

}
