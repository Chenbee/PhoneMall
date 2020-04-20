package com.cb.pmall.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.cb.pmall.beans.PmsSearchParam;
import com.cb.pmall.beans.PmsSearchSkuInfo;
import com.cb.pmall.beans.PmsSkuAttrValue;
import com.cb.pmall.service.SearchService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author 陈彬
 * date 2020/4/19 3:26 PM
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    JestClient jestClient;

    public  String getDslStr(PmsSearchParam pmsSearchParam){
        // 构建dsl工具
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 构建bool
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        // 构建filter
        // 获取catalogId
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        if (StringUtils.isNotBlank(catalog3Id)){
            // catalog3Id不为空
            TermQueryBuilder termQueryBuilder = new TermQueryBuilder("catalog3Id",catalog3Id);
            boolQueryBuilder.filter(termQueryBuilder);
        }
        // 获取attrValueId
        String[] valueIds = pmsSearchParam.getValueId();
        if (valueIds != null){
            for (String valueId : valueIds) {
                if (StringUtils.isNotBlank(valueId)){
                    // pmsSkuAttrValue不为空
                    TermQueryBuilder termQueryBuilder = new TermQueryBuilder("skuAttrValueList.valueId",valueId);
                    boolQueryBuilder.filter(termQueryBuilder);
                }
            }
        }

        // 构建must
        // 获取关键字
        String keyWord = pmsSearchParam.getKeyword();
        if (StringUtils.isNotBlank(keyWord)){
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName", keyWord);
            boolQueryBuilder.must(matchQueryBuilder);
        }
        // 设置query
        searchSourceBuilder.query(boolQueryBuilder);
        // 设置from
        searchSourceBuilder.from(0);
        // 设置size
        searchSourceBuilder.size(20);
        // 设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<span style ='color : red;'>");
        highlightBuilder.field("skuName");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlight(highlightBuilder);
        // 设置排序
        searchSourceBuilder.sort("productId",SortOrder.DESC);
        String delStr = searchSourceBuilder.toString();
        return delStr;
    }

    @Override
    public List<PmsSearchSkuInfo> searchList(PmsSearchParam pmsSearchParam) {
        // 获取query语句dsl
        String dslStr = getDslStr(pmsSearchParam);
        System.out.println(dslStr);
        Search search = new Search.Builder(dslStr).addIndex("pmall")
                .addType("PmsSearchSkuInfo")
                .build();

        List<PmsSearchSkuInfo> searchSkuInfoList = new ArrayList<>();
        try {
            // 调用jestClient进行查询
            SearchResult searchResult = jestClient.execute(search);
            // 将结果封装到PmsSearchInfoList
            List<SearchResult.Hit<PmsSearchSkuInfo, Void>> hits = searchResult.getHits(PmsSearchSkuInfo.class);
            for (SearchResult.Hit<PmsSearchSkuInfo, Void> hit : hits) {
                Map<String, List<String>> highlight = hit.highlight;
                PmsSearchSkuInfo source = hit.source;
                // 设置高亮
                if (highlight != null){
                    source.setSkuName(highlight.get("skuName").get(0));
                }
                searchSkuInfoList.add(source);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchSkuInfoList;
    }
}
