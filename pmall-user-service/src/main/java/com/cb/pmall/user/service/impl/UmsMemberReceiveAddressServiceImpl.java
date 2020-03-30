package com.cb.pmall.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.cb.pmall.beans.UmsMemberReceiveAddress;
import com.cb.pmall.service.UmsMemberReceiveAddressService;
import com.cb.pmall.user.mapper.UmsMemberReceiveAddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class UmsMemberReceiveAddressServiceImpl implements UmsMemberReceiveAddressService {

    @Autowired
    UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;

    @Override
    public List<UmsMemberReceiveAddress> getAllAddress(Integer memberId) {
        Example e = new Example(UmsMemberReceiveAddress.class);
        e.createCriteria().andEqualTo("memberId",memberId);
        return umsMemberReceiveAddressMapper.selectByExample(e);
    }

    @Override
    public UmsMemberReceiveAddress getAddress(Integer id) {
        return umsMemberReceiveAddressMapper.selectByPrimaryKey(id);
    }

    @Override
    public void deleteAddress(Integer id) {
        umsMemberReceiveAddressMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void modifyAddress(UmsMemberReceiveAddress umsMemberReceiveAddress) {
        umsMemberReceiveAddressMapper.updateByPrimaryKeySelective(umsMemberReceiveAddress);
    }

    @Override
    public void addAddress(UmsMemberReceiveAddress umsMemberReceiveAddress) {
        umsMemberReceiveAddressMapper.insert(umsMemberReceiveAddress);
    }
}