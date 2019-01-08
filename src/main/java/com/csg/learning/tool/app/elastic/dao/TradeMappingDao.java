package com.csg.learning.tool.app.elastic.dao;

import com.csg.learning.tool.app.elastic.model.TradeMapping;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.ReindexRequest;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class TradeMappingDao {

    private final Logger logger = LoggerFactory.getLogger(TradeMappingDao.class);

    @Value("${elasticsearch.index}")
    private String index;

    @Value("${elasticsearch.type}")
    private String type;

    @Value("${elasticsearch.index.archive}")
    private String archive;

    private RestHighLevelClient restHighLevelClient;

    private ObjectMapper objectMapper;

    public TradeMappingDao(ObjectMapper objectMapper, RestHighLevelClient restHighLevelClient) {
        this.objectMapper = objectMapper;
        this.restHighLevelClient = restHighLevelClient;
    }

    public TradeMapping insertTradeMapping(TradeMapping tradeMapping){

        tradeMapping.setId(tradeMapping.getSystem()+tradeMapping.getTid()+UUID.randomUUID().toString());
        Map<String, Object> dataMap = objectMapper.convertValue(tradeMapping, Map.class);
        IndexRequest indexRequest = new IndexRequest(index, type, tradeMapping.getId())
                .source(dataMap);
        try {
            IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch(ElasticsearchException e) {
            e.getDetailedMessage();
        } catch (java.io.IOException ex){
            ex.getLocalizedMessage();
        }
        logger.debug("successfully inserted TradeMapping with id: "+tradeMapping.getId());
        return tradeMapping;
    }


    public Map<String, Object> getTradeMappingById(String id){
        GetRequest getRequest = new GetRequest(index, type, id);
        GetResponse getResponse = null;
        try {
            getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        } catch (java.io.IOException e){
            e.getLocalizedMessage();
        }
        Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
        logger.debug("get TradeMapping: "+sourceAsMap.toString());
        return sourceAsMap;
    }

    public SearchResponse getTradeMappingBySystemAndTid(String system, String tid){

        SearchResponse searchResponse=null;
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(index);
        searchRequest.types(type);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchQuery("system", system)).must(QueryBuilders.matchQuery("tid", tid));
        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        } catch (java.io.IOException e){
            e.getLocalizedMessage();
        }
        logger.debug("getTradeMappingBySystemAndTid response: {} hits", searchResponse.getHits().getTotalHits());
        logger.trace("getTradeMappingBySystemAndTid response: {} ", searchResponse.toString());

        return searchResponse;
    }

    public SearchResponse getDuplicates(){

        SearchResponse searchResponse=null;
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(index);
        searchRequest.types(type);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.mustNot(QueryBuilders.existsQuery("metaInfo.validUntil")).mustNot(QueryBuilders.existsQuery("metaInfo.systemUntil"));

        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.aggregation(AggregationBuilders.terms("duplicates_system").field("system").minDocCount(2)
                .subAggregation(AggregationBuilders.terms("duplicates_tid").field("tid").minDocCount(2)));
               // .aggregation(AggregationBuilders.terms("agg2").field("tid").minDocCount(2));

        searchRequest.source(searchSourceBuilder);
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            logger.trace("getDuplicates response: {} ", searchResponse.toString());

        } catch (java.io.IOException e){
            e.getLocalizedMessage();
        }


        return searchResponse;
    }

    public SearchResponse getTradeMappingBySystemAndTidMap(Map<String,String> systemTid){

        SearchResponse searchResponse=null;
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(index);
        searchRequest.types(type);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        systemTid.forEach((system,tid)->{
            boolQueryBuilder.should(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("system", system)).must(QueryBuilders.matchQuery("tid", tid)));
        });

        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);

        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            logger.debug("getDuplicates response: {} hits", searchResponse.getHits().getTotalHits());
            logger.trace("getDuplicates response: {} ", searchResponse.toString());

        } catch (java.io.IOException e){
            e.getLocalizedMessage();
        }

        return searchResponse;
    }

    public Map<String, Object> updateTradeMappingById(String id, TradeMapping TradeMapping){
        UpdateRequest updateRequest = new UpdateRequest(index, type, id)
                .fetchSource(true);    // Fetch Object after its update
        Map<String, Object> error = new HashMap<>();
        error.put("Error", "Unable to update TradeMapping");
        try {
            String TradeMappingJson = objectMapper.writeValueAsString(TradeMapping);
            updateRequest.doc(TradeMappingJson, XContentType.JSON);
            UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
            return updateResponse.getGetResult().sourceAsMap();
        }catch (IOException e){
            logger.error("Unable to update TradeMapping with id: "+id);
            logger.error(e.getMessage());
        }
       return error;
    }

    public void deleteTradeMappingById(String id) {
        DeleteRequest deleteRequest = new DeleteRequest(index, type, id);
        try {
            DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (java.io.IOException e){
            e.getLocalizedMessage();
        }
    }

    public SearchResponse search(QueryBuilder query, Integer from, Integer size) throws IOException {
        logger.debug("elasticsearch query: {}", query.toString());
        SearchResponse response = restHighLevelClient.search(new SearchRequest("person")
                .source(new SearchSourceBuilder().query(query).from(from).size(size)), RequestOptions.DEFAULT);

        logger.debug("elasticsearch response: {} hits", response.getHits().getTotalHits());
        logger.trace("elasticsearch response: {} hits", response.toString());

        return response;
    }

    public void reindex()  {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.mustNot(QueryBuilders.existsQuery("metaInfo.validUntil")).mustNot(QueryBuilders.existsQuery("metaInfo.systemUntil"));

        ReindexRequest reindexRequest = new ReindexRequest();
        reindexRequest.setSourceQuery(boolQueryBuilder);
        reindexRequest.setSourceIndices(index);
        reindexRequest.setDestIndex(archive);
        reindexRequest.setRefresh(true);

        try {
            BulkByScrollResponse bulkResponse = restHighLevelClient.reindex(reindexRequest, RequestOptions.DEFAULT);
            logger.trace("elasticsearch response: {} ", bulkResponse.toString());
        } catch (java.io.IOException e){
            e.getLocalizedMessage();
        }

    }

    public void reindexByQuery(QueryBuilder query)  {
        logger.debug("elasticsearch query: {}", query.toString());

        ReindexRequest reindexRequest = new ReindexRequest();
        reindexRequest.setSourceQuery(query);
        reindexRequest.setSourceIndices(index);
        reindexRequest.setDestIndex(archive);
        reindexRequest.setRefresh(true);

        try {
            BulkByScrollResponse bulkResponse = restHighLevelClient.reindex(reindexRequest, RequestOptions.DEFAULT);
            logger.trace("elasticsearch response: {} ", bulkResponse.toString());
        } catch (java.io.IOException e){
            e.getLocalizedMessage();
        }
    }

    public void deleteByQuery(QueryBuilder query, String index)  {
        logger.debug("elasticsearch query: {}", query.toString());

        DeleteByQueryRequest request = new DeleteByQueryRequest(index);
        request.setQuery(query);
        request.setConflicts("proceed");

        try {
            BulkByScrollResponse bulkResponse = restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
            logger.trace("elasticsearch response: {} ", bulkResponse.toString());
        } catch (java.io.IOException e){
            e.getLocalizedMessage();
        }
    }
}
