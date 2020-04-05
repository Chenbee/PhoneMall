package com.cb.pmall.service;

import com.cb.pmall.beans.PmsBaseSaleAttr;
import com.cb.pmall.beans.PmsProductImage;
import com.cb.pmall.beans.PmsProductInfo;
import com.cb.pmall.beans.PmsProductSaleAttr;

import java.util.List;

/**
 * @author 陈彬
 * date 2020/4/1 4:45 PM
 */
public interface SpuService {
    // 查spu
    List<PmsProductInfo> spuList(String catalog3Id);

    // 存spu
    void saveSpuInfo(PmsProductInfo pmsProductInfo);

    // 删spu
    //      删除spu后也可删除sku(未删除)
    //      skuInfo通过 spuId删除
    //      通过skuid删除sku平台属性、销售属性、图片信息
    void delSpuById(String spuId);

    // 查spu销售属性
    List<PmsProductSaleAttr> spuSaleAttrList(String spuId);

    // 查spu图片URL
    List<PmsProductImage> spuImageList(String spuId);

    List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String spuId, String skuId);
}
