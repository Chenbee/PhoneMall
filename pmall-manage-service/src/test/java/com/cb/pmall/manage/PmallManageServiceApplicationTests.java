package com.cb.pmall.manage;

import com.cb.pmall.manage.service.impl.AttrServiceImpl;
import com.cb.pmall.service.AttrService;
import com.cb.pmall.utils.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.w3c.dom.Attr;
import redis.clients.jedis.Jedis;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PmallManageServiceApplicationTests {

    @Autowired
    RedisUtil redisUtil;


    @Test
    public void contextLoads() {
        Jedis jedis = redisUtil.getJedis();
        System.out.println(jedis.get("test"));
        jedis.close();
    }

    @Test
    public void tsds() {


       AttrService attrService = new AttrServiceImpl();

        //        System.out.println(addressService.getAllAddress("1"));
        System.out.println(attrService.getAttrValueList("1"));

    }

}
