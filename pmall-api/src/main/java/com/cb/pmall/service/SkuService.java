package com.cb.pmall.service;

import com.cb.pmall.beans.PmsBaseAttrInfo;
import com.cb.pmall.beans.PmsProductImage;
import com.cb.pmall.beans.PmsProductSaleAttr;
import com.cb.pmall.beans.PmsSkuInfo;

import java.util.List;

/**
 * @author 陈彬
 * date 2020/4/2 4:38 PM
 */
public interface SkuService {

    String saveSkuInfo(PmsSkuInfo pmsSkuInfo);

    PmsSkuInfo getSkuById(String skuId);

    List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(String spuId);
}
