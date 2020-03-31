package com.cb.pmall.service;

import com.cb.pmall.beans.PmsBaseAttrInfo;
import com.cb.pmall.beans.PmsBaseAttrValue;

import java.util.List;

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
}
