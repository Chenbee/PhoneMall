package com.cb.pmall.service;

import com.cb.pmall.beans.PmsBaseCatalog1;
import com.cb.pmall.beans.PmsBaseCatalog2;
import com.cb.pmall.beans.PmsBaseCatalog3;

import java.util.List;

/**
 * @author 陈彬
 * date 2020/3/31 12:46 PM
 */
public interface CatalogService {
    // 获取一级分类
    List<PmsBaseCatalog1> getCatalog1();
    // 获取二级分类
    List<PmsBaseCatalog2> getCatalog2(String catalog1Id);
    // 获取三级分类
    List<PmsBaseCatalog3> getCatalog3(String catalog2Id);

}
