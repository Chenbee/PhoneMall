package com.cb.pmall.user.Service.impl;

import com.cb.pmall.user.Service.UserService;
import com.cb.pmall.user.beans.UmsMember;
import com.cb.pmall.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public List<UmsMember> getAllUser() {
        return userMapper.selectAll();
    }
}
