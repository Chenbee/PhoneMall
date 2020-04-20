package com.cb.pmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.cb.pmall.beans.PmsBaseAttrInfo;
import com.cb.pmall.beans.PmsBaseAttrValue;
import com.cb.pmall.beans.PmsBaseSaleAttr;
import com.cb.pmall.manage.mapper.PmsBaseAttrInfoMapper;
import com.cb.pmall.manage.mapper.PmsBaseAttrValueMapper;
import com.cb.pmall.manage.mapper.PmsBaseSaleAttrMapper;
import com.cb.pmall.service.AttrService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Set;

/**
 * @author 陈彬
 * date 2020/3/31 2:56 PM
 */
@Service
public class AttrServiceImpl implements AttrService {

    @Autowired
    PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;

    @Autowired
    PmsBaseAttrValueMapper pmsBaseAttrValueMapper;

    @Autowired
    PmsBaseSaleAttrMapper pmsBaseSaleAttrMapper;



    @Override
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id) {
        PmsBaseAttrInfo pmsBaseAttrInfo = new PmsBaseAttrInfo();
        pmsBaseAttrInfo.setCatalog3Id(catalog3Id);
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsBaseAttrInfoMapper.select(pmsBaseAttrInfo);
        for (PmsBaseAttrInfo baseAttrInfo : pmsBaseAttrInfos) {
            String id = baseAttrInfo.getId();
            PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
            pmsBaseAttrValue.setAttrId(id);
            List<PmsBaseAttrValue> pmsBaseAttrValues = pmsBaseAttrValueMapper.select(pmsBaseAttrValue);
            // 将查出的pmsBaseAttrValues封装到baseAttrInfo
            baseAttrInfo.setAttrValueList(pmsBaseAttrValues);
        }
        return pmsBaseAttrInfos;
    }

    @Override
    public String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {
        String id = pmsBaseAttrInfo.getId();
        if (StringUtils.isBlank(id)) {// id为空,为增加操作
            // 添加属性
            pmsBaseAttrInfoMapper.insertSelective(pmsBaseAttrInfo);
            //取出属性值
            List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
            // 添加属性值
            for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                // 取出属性id作为属性值的外键
                pmsBaseAttrValue.setAttrId(pmsBaseAttrInfo.getId());
                // 插入属性值
                pmsBaseAttrValueMapper.insertSelective(pmsBaseAttrValue);
            }
        } else {// id不为空,为修改操作
            // 修改属性
            Example example = new Example(PmsBaseAttrValue.class);
            example.createCriteria().andEqualTo("id",pmsBaseAttrInfo.getId());
            pmsBaseAttrInfoMapper.updateByExampleSelective(pmsBaseAttrInfo,example);
            // 获取原先属性值
            List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
            // 修改属性值
            for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                if(pmsBaseAttrValue.getAttrId()==null){
                    // 如果id为空设置id并插入
                    pmsBaseAttrValue.setAttrId(pmsBaseAttrInfo.getId());
                    pmsBaseAttrValueMapper.insertSelective(pmsBaseAttrValue);
                }else {
                    // 否则id不为空根据主键修改
                    pmsBaseAttrValueMapper.updateByPrimaryKey(pmsBaseAttrValue);
                }
            }
        }

        return "success";
    }

    @Override
    public void deleteAttrInfoById(String attrId) {
        pmsBaseAttrInfoMapper.deleteByPrimaryKey(attrId);
        Example example = new Example(PmsBaseAttrValue.class);
        example.createCriteria().andEqualTo("attrId",attrId);
        pmsBaseAttrValueMapper.deleteByExample(attrId);
    }

    @Override
    public List<PmsBaseAttrValue> getAttrValueList(String attrId) {
        PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
        pmsBaseAttrValue.setAttrId(attrId);
        List<PmsBaseAttrValue> pmsBaseAttrValues = pmsBaseAttrValueMapper.select(pmsBaseAttrValue);
        return pmsBaseAttrValues;
    }

    @Override
    public List<PmsBaseSaleAttr> baseSaleAttrList() {
        return pmsBaseSaleAttrMapper.selectAll();
    }

    @Override
    public List<PmsBaseAttrInfo> getAttrInfoByValueId(Set<String> valueIdSet) {
        // 将valueId连接成字符串,用户后期数据库检索
        String valueIdStr = StringUtils.join(valueIdSet, ",");
        List<PmsBaseAttrInfo> attrInfoList = pmsBaseAttrInfoMapper.selectAttrInfoByValueId(valueIdStr);
        return attrInfoList;
    }
}
