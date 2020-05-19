package com.cb.pmall.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.cb.pmall.beans.UmsMember;
import com.cb.pmall.service.UmsMemberService;
import com.cb.pmall.user.mapper.UmsMemberMapper;
import com.cb.pmall.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.List;

@Service
public class UmsMemberServiceImpl implements UmsMemberService {

    @Autowired
    UmsMemberMapper umsMemberMapper;

    @Autowired
    RedisUtil redisUtil;

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

    @Override
    public UmsMember login(UmsMember umsMember) {
        Jedis jedis = redisUtil.getJedis();
        try {
            UmsMember user = null;
            // 从数据库中查询
            String member = jedis.get("user:" + umsMember.getUsername() + ":" + umsMember.getPassword());
            if (StringUtils.isNotBlank(member)) {
                // 缓存击中
                user = JSON.parseObject(member, UmsMember.class);
            } else {
                // 从数据库中查数据
                user = loginByDB(umsMember);
                // 将数据存入缓存
                if (user !=null){
                    jedis.setex("user:" + umsMember.getUsername() + ":" + umsMember.getPassword(), 60 * 30, JSON.toJSONString(user));
                }
            }
            return user;
        } finally {
            jedis.close();
        }
    }

    public UmsMember loginByDB(UmsMember umsMember) {
        // 获取数据
        List<UmsMember> memberList = umsMemberMapper.select(umsMember);
        if (memberList.size()>0) {
            return memberList.get(0);
        }
        // 未查出数据
        return null;
    }
}
