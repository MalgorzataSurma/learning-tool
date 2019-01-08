package com.csg.learning.tool.app.scheduling;

import com.csg.learning.tool.app.elastic.dao.TradeMappingDao;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class ScheduledTask {

    @Autowired
    private TradeMappingDao tradeMappingDao;
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTask.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(cron = "0 0 0 * * ?")
    public void executeTask() {
        logger.info("Task executed at {}", dateFormat.format(new Date()));

        SearchResponse searchResponse = tradeMappingDao.getDuplicates();

        Terms terms = searchResponse.getAggregations().get("duplicates_system");
        List<Terms.Bucket> buckets = (List<Terms.Bucket>) terms.getBuckets();
        buckets.forEach(elem-> {
            ((Terms) elem.getAggregations().get("duplicates_tid")).getBuckets().forEach(subElem -> {
                logger.warn("Duplicate found. System: "+elem.getKeyAsString() +" tid: "+subElem.getKeyAsString());
            });
        });
    }

}