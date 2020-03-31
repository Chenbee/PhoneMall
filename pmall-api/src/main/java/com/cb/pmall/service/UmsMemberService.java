package com.cb.pmall.service;

import com.cb.pmall.beans.UmsMember;

import java.util.List;

public interface UmsMemberService {

    // 获取所有用户
    List<UmsMember> getAllUser();

    // 获取单个用户
    UmsMember getUser(String id);

    // 删除单个用户
    void deleteUser(String id);

    // 修改一个用户
    void modifyUser(UmsMember umsMember);

    // 添加一个用户
    void addUser(UmsMember umsMember);
}
