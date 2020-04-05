package com.cb.pmall.manage.mapper;

import com.cb.pmall.beans.PmsSkuInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author 陈彬
 * date 2020/4/2 7:20 PM
 */
public interface PmsSkuInfoMapper extends Mapper<PmsSkuInfo> {
    List<PmsSkuInfo> selectSkuSaleAttrValueListBySpu(@Param("spuId") String spuId);
}
