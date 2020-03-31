package com.cb.pmall.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.cb.pmall.beans.UmsMember;
import com.cb.pmall.service.UmsMemberService;
import com.cb.pmall.user.mapper.UmsMemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
public class UmsMemberServiceImpl implements UmsMemberService {

    @Autowired
    UmsMemberMapper umsMemberMapper;

    @Override
    public List<UmsMember> getAllUser() {
        return umsMemberMapper.selectAll();
    }

    @Override
    public UmsMember getUser(String id) {
        return umsMemberMapper.selectByPrimaryKey(id);
    }

    @Override
    public void deleteUser(String id) {
        umsMemberMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void modifyUser(UmsMember umsMember) {
        umsMemberMapper.updateByPrimaryKey(umsMember);
    }

    @Override
    public void addUser(UmsMember umsMember) {
        umsMemberMapper.insert(umsMember);
    }
}
