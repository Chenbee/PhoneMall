package com.cb.pmall.user.Service;

import com.cb.pmall.user.beans.UmsMember;

import java.util.List;

public interface UmsMemberService {

    // 获取所有用户
    List<UmsMember> getAllUser();

    // 获取单个用户
    UmsMember getUser(Integer id);

    // 删除单个用户
    void deleteUser(Integer id);

    // 修改一个用户
    void modifyUser(UmsMember umsMember);

    // 添加一个用户
    void addUser(UmsMember umsMember);
}
