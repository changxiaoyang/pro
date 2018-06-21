package com.cxy890.elasticsearch.client;

import com.cxy890.config.annotation.AutoAssign;
import com.cxy890.config.annotation.AutoScan;
import com.cxy890.config.util.StringUtil;
import com.cxy890.elasticsearch.annotations.EsIndex;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import javax.naming.directory.SearchResult;
import java.util.List;

/**
 * @author BD-PC27
 */
@AutoScan
@Slf4j
public class ESRepository {

    @AutoAssign
    private Clients clients;

    public <T> void save(T entity, Class<T> clazz) {
        EsIndex index = EsParser.getIndex(clazz);
        if (index == null) {
            log.error("@EsIndex must be exist!");
            return;
        }
        clients.create(index, EsParser.getSource(clazz));
        IndexRequest request = new IndexRequest(index.value(), StringUtil.isNull(index.type())?index.value():index.type());
        request.id(EsParser.getId(entity, clazz));
        request.source(StringUtil.toJson(entity), XContentType.JSON);
        clients.getTransportClient().index(request);
    }

    public <T> List<T> search(SearchSourceBuilder queryBuilder) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("").types("").source(queryBuilder);
        SearchResponse searchResponse = clients.getTransportClient().search(searchRequest).actionGet();
        log.debug(searchResponse.getTook() + "ms");
        log.debug(searchResponse.getNumReducePhases() + "a");
        log.debug(searchResponse.getTotalShards() + "b");
        SearchHits hits = searchResponse.getHits();
        return null;
    }

    /**
     * 批量插入监听
     */
    BulkProcessor.Listener processListener = new BulkProcessor.Listener() {

        @Override
        public void beforeBulk(long executionId, BulkRequest request) {
        }

        @Override
        public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
            log.debug(String.format("Bulk request number of actions: %s response use time: %s", request.numberOfActions(),  response.getIngestTookInMillis()));
        }

        @Override
        public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
            log.error("Bulk request error, ", failure);
        }
    };

}
