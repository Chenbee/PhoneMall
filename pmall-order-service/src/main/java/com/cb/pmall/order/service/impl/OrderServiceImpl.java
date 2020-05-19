package com.cb.pmall.order.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.cb.pmall.beans.OmsOrder;
import com.cb.pmall.beans.OmsOrderItem;
import com.cb.pmall.order.mapper.OmsOrderItemMapper;
import com.cb.pmall.order.mapper.OmsOrderMapper;
import com.cb.pmall.service.OrderService;
import com.cb.pmall.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author 陈彬
 * date 2020/5/14 10:23 PM
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    OmsOrderMapper omsOrderMapper;

    @Autowired
    OmsOrderItemMapper omsOrderItemMapper;

    @Override
    public String getTradeCode(String memberId) {
        // 通过UUID创建出一个tradeCode存入到redis服务器后返回
        String tradeCode = UUID.randomUUID().toString();
        Jedis jedis = redisUtil.getJedis();
        String tradeKey = "user:" + memberId + ":tradeCode";
        jedis.setex(tradeKey, 60 * 30, tradeCode);
        jedis.close();
        return tradeCode;
    }

    @Override
    public String checkTradeCode(String memberId, String tradeCode) {
        Jedis jedis = redisUtil.getJedis();
        try {
            String tradeKey = "user:" + memberId + ":tradeCode";

            //String tradeCodeFromCache = jedis.get(tradeKey);// 使用lua脚本在发现key的同时将key删除，防止并发订单攻击
            //对比防重删令牌
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Long eval = (Long) jedis.eval(script, Collections.singletonList(tradeKey), Collections.singletonList(tradeCode));
            // eval=1说明有结果并且已删除
            if (eval != null && eval != 0) {
                return "success";
            } else {
                return "fail";
            }
        } finally {
            jedis.close();
        }
    }

    @Override
    public String saveOrder(OmsOrder omsOrder) {
        // 存入omsOrder
        omsOrderMapper.insertSelective(omsOrder);
        String orderId = omsOrder.getId();
        // 存入omsOrderItem
        List<OmsOrderItem> omsOrderItems = omsOrder.getOmsOrderItems();
        for (OmsOrderItem omsOrderItem : omsOrderItems) {
            omsOrderItem.setOrderId(orderId);
            omsOrderItemMapper.insertSelective(omsOrderItem);
        }
        return orderId;
    }

    @Override
    public OmsOrder getOrderById(String orderId) {
        return omsOrderMapper.selectByPrimaryKey(orderId);
    }

    @Override
    public void updateOrder(OmsOrder omsOrder) {
        Example example = new Example(OmsOrder.class);
        example.createCriteria().andEqualTo("id",omsOrder.getId());
        omsOrderMapper.updateByExampleSelective(omsOrder,example);
    }

}
