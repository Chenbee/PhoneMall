package com.cb.pmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.cb.pmall.beans.*;
import com.cb.pmall.manage.mapper.*;
import com.cb.pmall.service.SkuService;
import com.cb.pmall.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.UUID;

/**
 * @author 陈彬
 * date 2020/4/2 4:39 PM
 */
@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;

    @Autowired
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;

    @Autowired
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;

    @Autowired
    PmsSkuImageMapper pmsSkuImageMapper;

    @Autowired
    RedisUtil redisUtil;


    @Override
    public String saveSkuInfo(PmsSkuInfo pmsSkuInfo) {

        pmsSkuInfoMapper.insertSelective(pmsSkuInfo);
        String SkuId = pmsSkuInfo.getId();
        // 保存平台属性
        List<PmsSkuAttrValue> pmsSkuAttrValueList = pmsSkuInfo.getSkuAttrValueList();
        for (PmsSkuAttrValue pmsSkuAttrValue : pmsSkuAttrValueList) {
            pmsSkuAttrValue.setSkuId(SkuId);
            pmsSkuAttrValueMapper.insertSelective(pmsSkuAttrValue);
        }

        // 保存销售属性
        List<PmsSkuSaleAttrValue> pmsSkuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList();
        for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : pmsSkuSaleAttrValueList) {
            pmsSkuSaleAttrValue.setSkuId(SkuId);
            pmsSkuSaleAttrValueMapper.insert(pmsSkuSaleAttrValue);
        }

        // 保存图片
        List<PmsSkuImage> pmsSkuImageList = pmsSkuInfo.getSkuImageList();
        String skuDefaultImg = pmsSkuInfo.getSkuDefaultImg();
        for (PmsSkuImage pmsSkuImage : pmsSkuImageList) {
            if (pmsSkuImage.getImgUrl().equals(skuDefaultImg)){
                pmsSkuImage.setIsDefault("1");
            }
            pmsSkuImage.setSkuId(SkuId);
            pmsSkuImageMapper.insertSelective(pmsSkuImage);
        }
        return "success";
    }

    public PmsSkuInfo getSkuByDB(String skuId) {
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setId(skuId);
        PmsSkuInfo skuInfo = pmsSkuInfoMapper.selectOne(pmsSkuInfo);
        return skuInfo;
    }

    @Override
    public PmsSkuInfo getSkuById(String skuId,String ip) {
        // 先从Redis数据库获取数据
        Jedis jedis = redisUtil.getJedis();
        PmsSkuInfo  skuInfo = new PmsSkuInfo();
        try {
            String skuInfoJson = jedis.get("sku:" + skuId + ":info");
            // 有数据返回
            if (StringUtils.isNotBlank(skuInfoJson)){
                System.out.println("IP为:"+ip+"的用户从缓存中获取资源");
                // value存在
                skuInfo = JSON.parseObject(skuInfoJson,PmsSkuInfo.class);
            } else {
                // 查询结果为空,访问数据库
                // 防止缓存击穿设置分布式锁
                String token = UUID.randomUUID().toString();
                String OK = jedis.set("sku:" + skuId + ":lock", token, "nx", "px", 10*1000);// 拿到锁的线程有10秒的过期时间
                if (StringUtils.isNotBlank(OK) && OK.equals("OK")){
                    System.out.println("IP为:"+ip+"的用户没查到资源,获得锁:"+"sku:" + skuId + ":lock");
                    // 字符串不为空且等于OK
                    // 获得十秒访问数据库资格
                    skuInfo = getSkuByDB(skuId);
                    if (skuInfo!=null){
                        // 获取内容不为空,转为Json传到redis服务器
                        // Thread.sleep(5000); 测试分布式锁使用
                        System.out.println("IP为:"+ip+"的用户从数据库查到资源并存入缓存");
                        jedis.set("sku:"+skuId+":info",JSON.toJSONString(skuInfo));
                    }else {
                        // 查询结果为空
                        // 防止缓存穿透,设置为空字符串
                        jedis.setex("sku:"+skuId+":info",60*3,JSON.toJSONString(""));
                    }
                    // 查询完毕,释放锁
                    String lockToken = jedis.get("sku:" + skuId + ":lock");
                    if (StringUtils.isNotBlank(lockToken) && lockToken.equals(token)){
                        // 如果取出的值对比后为token,则删除锁
                        jedis.del("sku:"+skuId+"lock");
                        System.out.println("IP为:"+ip+"的用户查询完毕释放锁:"+"sku:" + skuId + ":lock");
                    }
                }else {
                    System.out.println("IP为:"+ip+"的用户没查到数据没抢到锁:sku:" + skuId + "lock,进入自旋");
                    // 未获得锁,自旋
                    Thread.sleep(3000);
                    return getSkuById(skuId,ip);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return skuInfo;
    }

    @Override
    public List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(String spuId) {
        return pmsSkuInfoMapper.selectSkuSaleAttrValueListBySpu(spuId);
    }

    @Override
    public List<PmsSkuImage> getSkuImageBySkuId(String skuId) {
        Example example = new Example(PmsSkuImage.class);
        example.createCriteria().andEqualTo("skuId",skuId);
        return pmsSkuImageMapper.selectByExample(example);
    }

    @Override
    public List<PmsSkuInfo> getAllSku() {
        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectAll();
        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {
            PmsSkuAttrValue pmsSkuAttrValue = new PmsSkuAttrValue();
            pmsSkuAttrValue.setSkuId(pmsSkuInfo.getId());
            List<PmsSkuAttrValue> select = pmsSkuAttrValueMapper.select(pmsSkuAttrValue);
            pmsSkuInfo.setSkuAttrValueList(select);
        }
        return pmsSkuInfos;
    }
}
