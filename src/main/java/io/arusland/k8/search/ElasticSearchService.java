package io.arusland.k8.search;

import io.arusland.k8.catalog.PropertyGetter;
import io.arusland.k8.catalog.SearchObject;
import org.apache.commons.lang3.Validate;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.CommonTermsQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.FilterBuilders.*;
import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.elasticsearch.search.aggregations.AggregationBuilders.*;

/**
 * Created by ruslan on 27.06.2015.
 */
@Service
public class ElasticSearchService implements SearchService {
    private static Logger logger = LoggerFactory.getLogger(ElasticSearchService.class);
    private static final String OBJECT_INDEX = "objects";
    private static final int SEARCH_ROWS = 25;
    private final Client client;
    private final ResultParser resultParser;

    @Autowired
    public ElasticSearchService(ResultParser resultParser) {
        this.resultParser = Validate.notNull(resultParser);

        NodeBuilder builder = NodeBuilder.nodeBuilder().local(true);
        builder.settings().put("path.data", "elastictestdata");
        client = builder.node().client();

        // TODO: if remove next lines tests fail with "Failed to execute phase [query], all shards failed"
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void index(SearchObject object) {
        logger.debug("INDEXING: " + object);

        Map<String, Object> doc = object.toDoc();

        IndexResponse response = client.prepareIndex(OBJECT_INDEX, object.GetSearchType(), object.getId())
                .setSource(doc)
                .setRefresh(true)
                .execute()
                .actionGet();
    }

    @Override
    public List<SearchObject> search(String searchText) {
        SearchRequestBuilder request = client.prepareSearch(OBJECT_INDEX);
        //request.setTypes(OBJECT_FILE);
        request.addFields(PropertyGetter.ALL_FIELDS);
        request.setSize(SEARCH_ROWS);
        request.setFrom(0);

        QueryBuilder esQuery = processQueryString(searchText);

        request.setQuery(esQuery);

        SearchResponse resp = request.execute().actionGet();
        SearchHits hits = resp.getHits();

        List<SearchObject> result = new LinkedList<>();

        for (SearchHit hit : hits) {
            SearchObject obj = resultParser.parse(hit);
            result.add(obj);
            logger.debug("FOUND: " + obj);
        }

        CountResponse respCount = client.prepareCount(OBJECT_INDEX)
                //.setTypes(OBJECT_FILE)
                .execute()
                .actionGet();

        logger.debug("All file objects count: " + respCount.getCount());

        return result;
    }

    private QueryBuilder processQueryString(String queryText) {
        if (queryText.isEmpty()) {
            return matchAllQuery();
        } else {
            CommonTermsQueryBuilder esQuery = commonTerms("_all", queryText);
            esQuery.lowFreqMinimumShouldMatch("2");

            MatchQueryBuilder phraseQuery = matchPhraseQuery("_all", queryText);
            phraseQuery.setLenient(true);

            BoolQueryBuilder boolQuery = boolQuery();
            boolQuery.should(esQuery);
            boolQuery.should(phraseQuery);

            return boolQuery;
        }
    }
}
