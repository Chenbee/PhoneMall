package com.cb.pmall.service;

import com.cb.pmall.beans.PmsBaseAttrInfo;
import com.cb.pmall.beans.PmsBaseAttrValue;
import com.cb.pmall.beans.PmsBaseSaleAttr;

import java.util.List;
import java.util.Set;

/**
 * @author 陈彬
 * date 2020/3/31 2:54 PM
 */
public interface AttrService {
    // 查
    List<PmsBaseAttrInfo> attrInfoList(String catalog3Id);

    // 增
    String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);

    // 删
    void deleteAttrInfoById(String attrId);

    // 改
    List<PmsBaseAttrValue> getAttrValueList(String attrId);

    //增加销售属性
    List<PmsBaseSaleAttr> baseSaleAttrList();

    List<PmsBaseAttrInfo> getAttrInfoByValueId(Set<String> valueIdSet);
}
