package com.csg.learning.tool.app.elastic.service;

import org.springframework.stereotype.Service;

@Service
public class TradeMappingService {

   /* private final Logger logger = LoggerFactory.getLogger(TradeMappingService.class);

    private final TradeMappingDao tradeMappingDao;

    private final TradeMappingRepository tradeMappingRepository;
    private final Mapper dozerBeanMapper;

    @Autowired
    public TradeMappingService(TradeMappingRepository tradeMappingRepository,
                         TradeMappingDao tradeMappingDao,
                         Mapper dozerBeanMapper) {
        this.tradeMappingRepository = tradeMappingRepository;
        this.dozerBeanMapper = dozerBeanMapper;
        this.tradeMappingDao = tradeMappingDao;
    }

    public TradeMapping get(Integer id) {
        TradeMapping tradeMapping = tradeMappingRepository.findById(id).orElseThrow(TradeMappingNotFoundException::new);
        logger.debug("get({})={}", id, tradeMapping);
        return tradeMapping;
    }

    public TradeMapping save(TradeMapping TradeMapping) {
        TradeMapping TradeMappingDb = tradeMappingRepository.save(TradeMapping);
        try {
            tradeMappingDao.insertTradeMapping(TradeMappingDb);
        } catch (Exception e) {
            logger.error("Houston, we have a problem!", e);
        }

        logger.debug("Saved TradeMapping [{}]", TradeMappingDb.getId());
        return TradeMappingDb;
    }

    public String search(String q, String f_country, String f_date, Integer from, Integer size) throws IOException {
        QueryBuilder query;

        if (Strings.isEmpty(q)) {
            query = QueryBuilders.matchAllQuery();
        } else {
            query = QueryBuilders
                    .multiMatchQuery(q);
        }

        if (Strings.hasText(f_country) || Strings.hasText(f_date)) {
            query = QueryBuilders.boolQuery().must(query);
            if (Strings.hasText(f_country)) {
                ((BoolQueryBuilder) query).filter(QueryBuilders.termQuery("address.country.aggs", f_country));
            }
            if (Strings.hasText(f_date)) {
                String endDate = "" + (Integer.parseInt(f_date) + 10);
                ((BoolQueryBuilder) query).filter(QueryBuilders.rangeQuery("dateOfBirth").gte(f_date).lt(endDate));
            }
        }

        SearchResponse response = tradeMappingDao.search(query, from, size);

        if (logger.isDebugEnabled()) logger.debug("search({},{},{})={} TradeMappings", q, f_country, f_date, response.getHits().getTotalHits());

        return response.toString();
    }*/
}
