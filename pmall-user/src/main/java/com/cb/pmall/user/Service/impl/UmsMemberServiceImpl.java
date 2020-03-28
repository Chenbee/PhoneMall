package com.cb.pmall.user.Service.impl;

import com.cb.pmall.user.Service.UmsMemberService;
import com.cb.pmall.user.beans.UmsMember;
import com.cb.pmall.user.mapper.UmsMemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public UmsMember getUser(Integer id) {
        return umsMemberMapper.selectByPrimaryKey(id);
    }

    @Override
    public void deleteUser(Integer id) {
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
