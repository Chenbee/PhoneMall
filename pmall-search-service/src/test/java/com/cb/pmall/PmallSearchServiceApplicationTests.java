package com.cb.pmall;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cb.pmall.beans.PmsSearchSkuInfo;
import com.cb.pmall.beans.PmsSkuInfo;
import com.cb.pmall.mq.ActiveMQUtil;
import com.cb.pmall.service.SkuService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.commons.beanutils.BeanUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jms.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PmallSearchServiceApplicationTests {

    @Reference
    SkuService skuService;

    @Autowired
    JestClient jestClient;
    @Autowired
    ActiveMQUtil activeMQUtil;

    @Test
    public void contextLoads() {

    }

    @Test
    public void get() throws IOException {

        // jest的dsl工具
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // 获取BoolQueryBuilder
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        // bool的filter
        TermQueryBuilder termQueryBuilder = new TermQueryBuilder("catalog3Id", "61");
        boolQueryBuilder.filter(termQueryBuilder);

        // bool的must
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName", "华为");
        boolQueryBuilder.must(matchQueryBuilder);
        // 构建query,将bool加入query
        searchSourceBuilder.query(boolQueryBuilder);

        // from
        searchSourceBuilder.from(0);
        // size
        searchSourceBuilder.size(20);

        SearchSourceBuilder highlight = searchSourceBuilder.highlight(null);

        // 将query转成String
        String dslStr = searchSourceBuilder.toString();
        System.out.println(dslStr);
        //获取Action(Search)
        Search search = new Search.Builder(dslStr).addIndex("pmall").addType("PmsSearchSkuInfo").build();

        // 执行查询
        SearchResult searchResult = jestClient.execute(search);

        // 返回集合
        List<PmsSearchSkuInfo> pmsSearchSkuInfoList = new ArrayList<>();
        // 获取数据
        List<SearchResult.Hit<PmsSearchSkuInfo, Void>> hits = searchResult.getHits(PmsSearchSkuInfo.class);

        for (SearchResult.Hit<PmsSearchSkuInfo, Void> hit : hits) {
            PmsSearchSkuInfo source = hit.source;
            pmsSearchSkuInfoList.add(source);
        }
        System.out.println(pmsSearchSkuInfoList);
    }

    @Test
    public void put() throws InvocationTargetException, IllegalAccessException, IOException {
        // 从mysql中查出数据
        List<PmsSkuInfo> skuInfos = skuService.getAllSku();

        // 将数据封装成ElasticSearch格式的Bean
        // 创建PmsSearchSkuInfo
        List<PmsSearchSkuInfo> pmsSearchSkuInfos = new ArrayList<>();
        for (PmsSkuInfo skuInfo : skuInfos) {
            // 转换Bean
            PmsSearchSkuInfo pmsSearchSkuInfo = new PmsSearchSkuInfo();
            BeanUtils.copyProperties(pmsSearchSkuInfo, skuInfo);
            pmsSearchSkuInfo.setProductId(skuInfo.getSpuId());
            pmsSearchSkuInfos.add(pmsSearchSkuInfo);
        }
        // 将数据存入ElasticSearch
        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfos) {
            Index index = new Index.Builder(pmsSearchSkuInfo).index("pmall").type("PmsSearchSkuInfo").id(pmsSearchSkuInfo.getId() + "").build();
            jestClient.execute(index);
        }
    }

    @Test
    public void testmq() {
        // 发送消息,同步ElasticSearch中的mapping
        Connection connection = null;
        Session session = null;
        try {
            // 创建连接
            connection = activeMQUtil.getConnectionFactory().createConnection();
            // 创建session
            session = connection.createSession(true, Session.SESSION_TRANSACTED);
        } catch (JMSException e) {
            e.printStackTrace();
        }

        try {
            Queue put_mapping = session.createQueue("PUT_MAPPING");
            MessageProducer producer = session.createProducer(put_mapping);
            MapMessage mapMessage = new ActiveMQMapMessage();// hash结构
            mapMessage.setString("skuId", "1");
            producer.send(mapMessage);
            // 未出错则提交
            session.commit();
        } catch (JMSException e) {
            try {
                // 出错则回滚
                session.rollback();
            } catch (JMSException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                // 最后关闭session与Connection
                session.close();
                connection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

}
