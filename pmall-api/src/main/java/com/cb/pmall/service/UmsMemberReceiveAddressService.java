package com.cb.pmall.service;

import com.cb.pmall.beans.UmsMemberReceiveAddress;

import java.util.List;

public interface UmsMemberReceiveAddressService {

    // 获取单个用户所有地址
    List<UmsMemberReceiveAddress> getAllAddress(String memberId);

    // 根据id获取单个地址
    UmsMemberReceiveAddress getAddress(String id);

    // 删除单个地址
    void deleteAddress(String id);

    // 修改单个地址
    void modifyAddress(UmsMemberReceiveAddress umsMemberReceiveAddress);

    // 添加一个地址
    void addAddress(UmsMemberReceiveAddress umsMemberReceiveAddress);
}
