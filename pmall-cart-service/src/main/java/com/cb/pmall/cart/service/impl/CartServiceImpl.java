package com.cb.pmall.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.cb.pmall.beans.OmsCartItem;
import com.cb.pmall.cart.mapper.OmsCartItemMapper;
import com.cb.pmall.service.CartService;
import com.cb.pmall.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * @author 陈彬
 * date 2020/4/24 11:16 PM
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    OmsCartItemMapper omsCartItemMapper;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public OmsCartItem isOmsCartItemExsitByUserId(String memberId, String skuId) {
        OmsCartItem cartItem = new OmsCartItem();
        cartItem.setMemberId(memberId);
        cartItem.setProductSkuId(skuId);
        OmsCartItem omsCartItem = omsCartItemMapper.selectOne(cartItem);
        return omsCartItem;
    }

    @Override
    public void addCart(OmsCartItem omsCartItem) {
        omsCartItemMapper.insertSelective(omsCartItem);

    }

    @Override
    public void updateCart(OmsCartItem omsCartItemByDB) {
        Example example = new Example(OmsCartItem.class);
        example.createCriteria().andEqualTo("id", omsCartItemByDB.getId());
        omsCartItemMapper.updateByExampleSelective(omsCartItemByDB, example);
    }

    @Override
    public void flushDB(String memberId) {
        // 从数据库中查出但前用户购物车下下所有的sku
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        List<OmsCartItem> omsCartItems = omsCartItemMapper.select(omsCartItem);
        Map<String, String> map = new HashMap<>();
        // 创建jedis客户端
        Jedis jedis = redisUtil.getJedis();
        try {
            // 循环omsCartItems
            for (OmsCartItem cartItem : omsCartItems) {
                cartItem.setTotalPrice(cartItem.getPrice().multiply(cartItem.getQuantity()));
                map.put(cartItem.getProductSkuId(), JSON.toJSONString(cartItem));
            }
            // 设置user:memberId:cart的hashMap表
            jedis.del("user:" + memberId + ":cart");
            jedis.hmset("user:" + memberId + ":cart", map);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
    }

    @Override
    public void flushDBIsCheckOnly(String memberId,String skuId) {
        // 创建jedis工具
        Jedis jedis = redisUtil.getJedis();
        try {
            OmsCartItem omsCartItem = new OmsCartItem();
            omsCartItem.setMemberId(memberId);
            omsCartItem.setProductSkuId(skuId);
            // 查出omsCartItem
            OmsCartItem omsCartItem1 = omsCartItemMapper.selectOne(omsCartItem);
            Map<String,String > map = new HashMap<>();
            map.put(skuId,JSON.toJSONString(omsCartItem1));
            jedis.hmset("user:" + memberId + ":cart", map);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            jedis.close();
        }
    }


    @Override
    public List<OmsCartItem> cartList(String memberId) {
        // 创建jedis工具
        Jedis jedis = redisUtil.getJedis();
        // 创建集合
        List<OmsCartItem> omsCartItems = new ArrayList<>();
        try {
            // 通过jedis获取所有的omsCartItem
            List<String> hvals = jedis.hvals("user:" + memberId + ":cart");
            if (hvals != null && hvals.size() != 0) {
                // 缓存中有值,直接返回
                System.out.println("缓存有值直接返回");
                for (String hval : hvals) {
                    OmsCartItem omsCartItem = JSON.parseObject(hval, OmsCartItem.class);
                    omsCartItems.add(omsCartItem);
                    // 对检索结果进行排序
                    omsCartItems.sort(new Comparator<OmsCartItem>() {
                        @Override
                        public int compare(OmsCartItem o1, OmsCartItem o2) {
                            int parm1 = Integer.parseInt(o1.getProductSkuId());
                            int parm2 = Integer.parseInt(o2.getProductSkuId());
                            return parm1-parm2;
                        }
                    });
                }
            } else {
                // 缓存中无值
                // 申请分布式锁
                String OK = jedis.set("user:" + memberId + ":lock", "1", "nx", "px", 10 * 1000);// 拿到锁的线程有10秒的过期时间
                if (StringUtils.isNotBlank(OK) && OK.equals("OK")) {
                    System.out.println("该线程抢到了锁");
                    // 该线程获取锁获取十秒钟访问数据库的权利
                    omsCartItems = cartListFromDB(memberId);
                    for (OmsCartItem omsCartItem : omsCartItems) {
                        omsCartItem.setTotalPrice(omsCartItem.getPrice().multiply(omsCartItem.getQuantity()));
                    }
                    if (omsCartItems != null) {
                        // 获取到值,更新缓存
                        flushDB(memberId);
                    } else {
                        // 防止缓存穿透,设置null值
                        jedis.setex("user:" + memberId + ":cart", 60 * 3, JSON.toJSONString(""));
                    }
                    // 查询结束,释放锁
                        jedis.del("user:" + memberId + ":lock");
                        System.out.println("查询结束,释放锁");

                } else {
                    // 未抢到锁
                    // 自旋
                    System.out.println("未抢到锁,自旋");
                    return cartList(memberId);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }

        return omsCartItems;
    }

    @Override
    public void checkCart(OmsCartItem omsCartItem) {
        Example example = new Example(OmsCartItem.class);
        example.createCriteria().andEqualTo("memberId",omsCartItem.getMemberId()).andEqualTo("productSkuId",omsCartItem.getProductSkuId());
        omsCartItemMapper.updateByExampleSelective(omsCartItem,example);
    }

    @Override
    public void removeItems(List<OmsCartItem> omsCartItemDelList,String memberId) {
        for (OmsCartItem omsCartItem : omsCartItemDelList) {
            // 从数据库中移除
            omsCartItemMapper.delete(omsCartItem);
        }
        // 从redis服务器中移除
        Jedis jedis = redisUtil.getJedis();
        String delKey = "user:" + memberId + ":cart";
        jedis.del(delKey);
        jedis.close();

    }

    private List<OmsCartItem> cartListFromDB(String memberId) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        List<OmsCartItem> select = omsCartItemMapper.select(omsCartItem);
        return select;

    }
}
