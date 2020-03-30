package com.cb.pmall.service;

import com.cb.pmall.beans.UmsMemberReceiveAddress;

import java.util.List;

public interface UmsMemberReceiveAddressService {

    // 获取单个用户所有地址
    List<UmsMemberReceiveAddress> getAllAddress(Integer memberId);

    // 根据id获取单个地址
    UmsMemberReceiveAddress getAddress(Integer id);

    // 删除单个地址
    void deleteAddress(Integer id);

    // 修改单个地址
    void modifyAddress(UmsMemberReceiveAddress umsMemberReceiveAddress);

    // 添加一个地址
    void addAddress(UmsMemberReceiveAddress umsMemberReceiveAddress);
}
