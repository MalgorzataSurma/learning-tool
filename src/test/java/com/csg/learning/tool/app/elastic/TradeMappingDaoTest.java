package com.csg.learning.tool.app.elastic;

import com.csg.learning.tool.app.elastic.dao.TradeMappingDao;
import com.csg.learning.tool.app.elastic.model.MetaInfo;
import com.csg.learning.tool.app.elastic.model.TradeMapping;
import org.apache.commons.lang.RandomStringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TradeMappingDaoTest {

    @Autowired
    private TradeMappingDao tradeMappingDao;


    @Before
    public void before() {
        TradeMapping tradeMapping = new TradeMapping();
        tradeMapping.setTid("ABC");
        tradeMapping.setBu("0000");
        tradeMapping.setSystem("ZXC");
        tradeMapping.setCif("890809790123");
        MetaInfo metaInfo = new MetaInfo(new Date(), new Date(), new Date(), new Date());
        //tradeMapping.setMetaInfo(metaInfo);
        tradeMappingDao.insertTradeMapping(tradeMapping);

        TradeMapping tradeMapping2 = new TradeMapping();
        tradeMapping2.setTid("ABCABC");
        tradeMapping2.setBu("0000");
        tradeMapping2.setSystem("ZXCZXC");
        tradeMapping2.setCif("890809790123");
        MetaInfo metaInfo2 = new MetaInfo(new Date(), new Date(), new Date(), new Date());
        // tradeMapping.setMetaInfo(metaInfo2);
        tradeMappingDao.insertTradeMapping(tradeMapping2);
    }

    @Test
    public void testSave() {
        TradeMapping tradeMapping = new TradeMapping();
        tradeMapping.setTid("ABC");
        tradeMapping.setBu("0000");
        tradeMapping.setSystem("ZXC");
        tradeMapping.setCif("7778");
        MetaInfo metaInfo = new MetaInfo(new Date(), new Date(), new Date(), new Date());
        tradeMapping.setMetaInfo(metaInfo);
        tradeMappingDao.insertTradeMapping(tradeMapping);
    }

    @Test
    public void testSave100() {
        TradeMapping tradeMapping = new TradeMapping();

        for (int i = 0; i < 10; i++) {

            tradeMapping.setTid(RandomStringUtils.random(10, true, true).toUpperCase());
            tradeMapping.setBu(RandomStringUtils.random(4, false, true).toUpperCase());
            tradeMapping.setSystem(RandomStringUtils.random(5, true, false).toUpperCase());
            tradeMapping.setCif(RandomStringUtils.random(5, false, true).toUpperCase());
            tradeMappingDao.insertTradeMapping(tradeMapping);
        }
    }

    @Test
    public void testGetById() {

        Map<String, Object> tradeMapping = tradeMappingDao.getTradeMappingById("ZXCABCda7ed261-ba22-4094-9c8e-431285239bd9");
        assertThat(tradeMapping).isNotNull();
    }

    @Test
    public void testGetBySystemAndTid() {

        SearchResponse tradeMappings = tradeMappingDao.getTradeMappingBySystemAndTid("ZXC", "ABC");
        assertThat(tradeMappings).isNotNull();
    }

    @Test
    public void testGetBySystemAndTidMap() {


        Map<String, String> systemTid = new HashMap<>();
        systemTid.put("ZXC", "ABC");
        systemTid.put("ZXCZXC", "ABCABC");
        SearchResponse tradeMappings = tradeMappingDao.getTradeMappingBySystemAndTidMap(systemTid);
        assertThat(tradeMappings).isNotNull();
        assertThat(tradeMappings.getHits().totalHits >= 2).isTrue();
    }

    @Test
    public void testDuplicates() {

        SearchResponse searchResponse = tradeMappingDao.getDuplicates();

        //Aggregations aggResult = searchResponse.getAggregations().get("duplicates_tid");
        Terms terms = searchResponse.getAggregations().get("duplicates_system");
        List<Terms.Bucket> buckets = (List<Terms.Bucket>) terms.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            System.out.println(bucket.getKeyAsString() + " (" + bucket.getDocCount() + ")");
            Terms terms2 = bucket.getAggregations().get("duplicates_tid");
            Collection<Terms.Bucket> bkts = (List<Terms.Bucket>) terms2.getBuckets();
            bkts.forEach(elem -> {
                System.out.println(elem.getKeyAsString());
            });

        }
        assertThat(1 == 1);

    }

    @Test
    public void testReindex() {
        tradeMappingDao.reindex();
    }

    @Test
    public void testReindexByQuery() {
        Map<String, String> systemTid = new HashMap<>();
        systemTid.put("ZXC", "ABC");
        systemTid.put("ZXCZXC", "ABCABC");

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.mustNot(QueryBuilders.existsQuery("metaInfo.validUntil")).mustNot(QueryBuilders.existsQuery("metaInfo.systemUntil"));
        systemTid.forEach((system, tid) -> {
            boolQueryBuilder.should(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("system", system)).must(QueryBuilders.matchQuery("tid", tid)));
        });

        tradeMappingDao.reindexByQuery(boolQueryBuilder);

    }

    @Test
    public void testDeleteByQuery() {
        Map<String, String> systemTid = new HashMap<>();
        systemTid.put("ZXC", "ABC");
        systemTid.put("ZXCZXC", "ABCABC");

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.mustNot(QueryBuilders.existsQuery("metaInfo.validUntil")).mustNot(QueryBuilders.existsQuery("metaInfo.systemUntil"));
        systemTid.forEach((system, tid) -> {
            boolQueryBuilder.should(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("system", system)).must(QueryBuilders.matchQuery("tid", tid)));
        });

        tradeMappingDao.deleteByQuery(boolQueryBuilder, "trade-mapping-archive");

    }



}

