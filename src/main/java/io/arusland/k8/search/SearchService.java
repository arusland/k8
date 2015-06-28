package io.arusland.k8.search;

import io.arusland.k8.catalog.SearchObject;
import io.arusland.k8.catalog.fs.FileSearchObject;
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
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static org.elasticsearch.index.query.FilterBuilders.*;
import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.elasticsearch.search.aggregations.AggregationBuilders.*;

/**
 * Created by ruslan on 27.06.2015.
 */
@Service
public class SearchService {
    private static final String OBJECT_INDEX = "objects";
    private static final String OBJECT_FILE = "file";
    private static final int SEARCH_ROWS = 25;
    private final Client client;

    public SearchService() {
        NodeBuilder builder = NodeBuilder.nodeBuilder().local(true);

        builder.settings().put("path.data", "elasticdata");

        client = builder.node().client();
    }

    public void index(SearchObject object) {
        Map<String, Object> doc = object.toDoc();

        IndexResponse response = client.prepareIndex(OBJECT_INDEX, OBJECT_FILE, object.getId())
                .setSource(doc)
                .execute()
                .actionGet();
    }

    public void search(String searchText) {
        SearchRequestBuilder request = client.prepareSearch(OBJECT_INDEX);
        request.setTypes(OBJECT_FILE);
        request.addFields(FileSearchObject.getFields());
        request.setSize(SEARCH_ROWS);
        request.setFrom(0);

        QueryBuilder esQuery = processQueryString(searchText);

        request.setQuery(esQuery);

        SearchResponse resp = request.execute().actionGet();
        SearchHits hits = resp.getHits();

        for (SearchHit hit : hits){
            SearchObject obj = ResultParser.parse(hit);
            System.out.println(obj);
        }

        CountResponse respCount = client.prepareCount(OBJECT_INDEX)
                .setTypes(OBJECT_FILE)
                .execute()
                .actionGet();

        System.out.println("All file objects count: " + respCount.getCount());
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
