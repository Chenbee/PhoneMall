package com.cb.pmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.cb.pmall.beans.PmsBaseAttrInfo;
import com.cb.pmall.beans.PmsBaseAttrValue;
import com.cb.pmall.manage.mapper.PmsBaseAttrInfoMapper;
import com.cb.pmall.manage.mapper.PmsBaseAttrValueMapper;
import com.cb.pmall.service.AttrService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

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

    @Override
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id) {
        PmsBaseAttrInfo pmsBaseAttrInfo = new PmsBaseAttrInfo();
        pmsBaseAttrInfo.setCatalog3Id(catalog3Id);
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsBaseAttrInfoMapper.select(pmsBaseAttrInfo);
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
                pmsBaseAttrValueMapper.updateByPrimaryKey(pmsBaseAttrValue);
            }
        }

        return "success";
    }

    @Override
    public void deleteAttrInfoById(String attrId) {
        pmsBaseAttrInfoMapper.deleteByPrimaryKey(attrId);
    }

    @Override
    public List<PmsBaseAttrValue> getAttrValueList(String attrId) {
        PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
        pmsBaseAttrValue.setAttrId(attrId);
        List<PmsBaseAttrValue> pmsBaseAttrValues = pmsBaseAttrValueMapper.select(pmsBaseAttrValue);
        return pmsBaseAttrValues;
    }
}
