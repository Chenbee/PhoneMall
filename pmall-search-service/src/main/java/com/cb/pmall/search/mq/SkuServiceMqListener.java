package com.cb.pmall.search.mq;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cb.pmall.beans.PmsSearchSkuInfo;
import com.cb.pmall.beans.PmsSkuInfo;
import com.cb.pmall.service.AttrService;
import com.cb.pmall.service.SkuService;
import com.cb.pmall.service.SpuService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author 陈彬
 * date 2020/5/19 6:42 PM
 */
@Component
public class SkuServiceMqListener {
    @Reference
    SkuService skuService;

    @Reference
    AttrService attrService;

    @Autowired
    JestClient jestClient;

    @JmsListener(destination = "PUT_MAPPING", containerFactory = "jmsQueueListener")
    public void consumeNewSku(MapMessage mapMessage) throws JMSException, InvocationTargetException, IllegalAccessException, IOException {
        String skuId = mapMessage.getString("skuId");
        // 从mysql中查出数据
        PmsSkuInfo skuInfo = skuService.getSkuById(skuId, null);

        // 将数据封装成ElasticSearch格式的Bean
        // 创建PmsSearchSkuInfo
        // 转换Bean
        PmsSearchSkuInfo pmsSearchSkuInfo = new PmsSearchSkuInfo();
        BeanUtils.copyProperties(pmsSearchSkuInfo, skuInfo);
        pmsSearchSkuInfo.setProductId(skuInfo.getSpuId());
        // 将数据存入ElasticSearch
        Index index = new Index.Builder(pmsSearchSkuInfo).index("pmall").type("PmsSearchSkuInfo").id(skuId).build();
        jestClient.execute(index);
    }
}
