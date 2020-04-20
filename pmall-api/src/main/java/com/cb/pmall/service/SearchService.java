package com.cb.pmall.service;

import com.cb.pmall.beans.PmsSearchParam;
import com.cb.pmall.beans.PmsSearchSkuInfo;

import java.util.List;

/**
 * @author 陈彬
 * date 2020/4/19 3:23 PM
 */
public interface SearchService {
    List<PmsSearchSkuInfo> searchList(PmsSearchParam pmsSearchParam);
}
