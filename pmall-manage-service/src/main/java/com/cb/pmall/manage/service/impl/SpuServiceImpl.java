package com.cb.pmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.cb.pmall.beans.*;
import com.cb.pmall.manage.mapper.PmsProductImageMapper;
import com.cb.pmall.manage.mapper.PmsProductInfoMapper;
import com.cb.pmall.manage.mapper.PmsProductSaleAttrMapper;
import com.cb.pmall.manage.mapper.PmsProductSaleAttrValueMapper;
import com.cb.pmall.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author 陈彬
 * date 2020/4/1 4:45 PM
 */
@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    PmsProductInfoMapper pmsProductInfoMapper;

    @Autowired
    PmsProductSaleAttrMapper pmsProductSaleAttrMapper;

    @Autowired
    PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;

    @Autowired
    PmsProductImageMapper pmsProductImageMapper;

    @Override
    public List<PmsProductInfo> spuList(String catalog3Id) {
        PmsProductInfo pmsProductInfo = new PmsProductInfo();
        pmsProductInfo.setCatalog3Id(catalog3Id);
        List<PmsProductInfo> productInfos = pmsProductInfoMapper.select(pmsProductInfo);
        return productInfos;
    }


    @Override
    public void saveSpuInfo(PmsProductInfo pmsProductInfo) {
        String id = pmsProductInfo.getId();
        // id为空,保存操作
        // 保存spu信息
        pmsProductInfoMapper.insertSelective(pmsProductInfo);

        // 取出产品id,存属性时使用
        String productId = pmsProductInfo.getId();

        //取出图片信息并保存
        List<PmsProductImage> pmsProductImageList = pmsProductInfo.getSpuImageList();
        for (PmsProductImage pmsProductImage : pmsProductImageList) {
            // 设置产品id
            pmsProductImage.setProductId(productId);
            pmsProductImageMapper.insertSelective(pmsProductImage);
        }

        // 取出spu的销售属性并保存
        List<PmsProductSaleAttr> pmsProductSaleAttrList = pmsProductInfo.getSpuSaleAttrList();
        for (PmsProductSaleAttr pmsProductSaleAttr : pmsProductSaleAttrList) {
            pmsProductSaleAttr.setProductId(productId);
            pmsProductSaleAttrMapper.insertSelective(pmsProductSaleAttr);
            // 取出销售属性里的属性值并保存
            List<PmsProductSaleAttrValue> pmsProductSaleAttrValueList = pmsProductSaleAttr.getSpuSaleAttrValueList();
            for (PmsProductSaleAttrValue pmsProductSaleAttrValue : pmsProductSaleAttrValueList) {
                pmsProductSaleAttrValue.setProductId(productId);
                pmsProductSaleAttrValueMapper.insertSelective(pmsProductSaleAttrValue);
            }
        }


    }

    @Override
    public void delSpuById(String spuId) {
        // 删除spu
        pmsProductInfoMapper.deleteByPrimaryKey(spuId);

        // 删除spu下的image
        Example pmsProductImage = new Example(PmsProductImage.class);
        pmsProductImage.createCriteria().andEqualTo("productId", spuId);
        pmsProductImageMapper.deleteByExample(pmsProductImage);

        // 删除spu下的sale_attr
        Example pmsProductSaleAttr = new Example(PmsProductSaleAttr.class);
        pmsProductSaleAttr.createCriteria().andEqualTo("productId", spuId);
        pmsProductSaleAttrMapper.deleteByExample(pmsProductSaleAttr);

        // 删除spu下的sale_attr_value
        Example pmsProductSaleAttrValue = new Example(PmsProductSaleAttrValue.class);
        pmsProductSaleAttrValue.createCriteria().andEqualTo("productId", spuId);
        pmsProductSaleAttrValueMapper.deleteByExample(pmsProductSaleAttrValue);


    }

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId) {
        PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
        pmsProductSaleAttr.setProductId(spuId);
        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.select(pmsProductSaleAttr);
        String saleAttrId = null;
        PmsProductSaleAttrValue pmsProductSaleAttrValue = null;
        List<PmsProductSaleAttrValue> pmsProductSaleAttrValues = null;
        for (PmsProductSaleAttr productSaleAttr : pmsProductSaleAttrs) {
            // 获取saleAttrId
            saleAttrId = productSaleAttr.getSaleAttrId();

            // 查出销售属性值
            pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
            pmsProductSaleAttrValue.setSaleAttrId(saleAttrId);
            pmsProductSaleAttrValue.setProductId(spuId);
            pmsProductSaleAttrValues = pmsProductSaleAttrValueMapper.select(pmsProductSaleAttrValue);

            // 保存销售属性值
            productSaleAttr.setSpuSaleAttrValueList(pmsProductSaleAttrValues);
        }


        return pmsProductSaleAttrs;
    }

    @Override
    public List<PmsProductImage> spuImageList(String spuId) {
        // 查销售属性并封装销售属性值
        // 销售属性
        Example example1 = new Example(PmsProductImage.class);
        example1.createCriteria().andEqualTo("productId", spuId);
        List<PmsProductImage> pmsProductSaleAttrs = pmsProductImageMapper.selectByExample(example1);


        return pmsProductSaleAttrs;
    }
}
