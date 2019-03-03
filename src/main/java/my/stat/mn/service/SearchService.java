package my.stat.mn.service;

import io.micronaut.tracing.annotation.NewSpan;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import java.time.LocalDateTime;
import javax.inject.Inject;
import lombok.AllArgsConstructor;
import my.stat.mn.data.Status;
import org.bson.types.ObjectId;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 *
 * @author naoki
 */
public class SearchService {
    
    @Inject
    RestHighLevelClient client;
    
    @NewSpan("es.createIndex")
    public Single<IndexResponse> createIndex(Status status) {
        var req = new IndexRequest("mystat", "status", status.getId().toString())
                .source("id", status.getId().toString(),
                        "userHandle", status.getUserHandle(),
                        "text", status.getText(),
                        "createdAt", status.getCreatedAt());
        return indexReq(req);
    }
    
    @NewSpan("es.search")
    public Flowable<Status> search(String keyword) {
        var req = new SearchRequest("mystat");
        var qb = QueryBuilders.termQuery("text", keyword);
        req.source(new SearchSourceBuilder().query(qb));
        return search(req).flattenAsFlowable(resp -> resp.getHits())
                .map(hit -> Status.builder()
                    .id(new ObjectId(hit.getId()))
                    .userHandle(hit.getSourceAsMap().getOrDefault("userHandle", "").toString())
                    .text(hit.getSourceAsMap().getOrDefault("text", "").toString())
                    .createdAt(LocalDateTime.parse(hit.getSourceAsMap().getOrDefault("createdAt", LocalDateTime.now().toString()).toString()))
                    .build());
    }
    
    Single<IndexResponse> indexReq(IndexRequest req) {
        return Single.create(se -> 
            client.indexAsync(req, RequestOptions.DEFAULT, new SearchActionListener<>(se)));
    }
    Single<SearchResponse> search(SearchRequest req) {
        return Single.create(se -> 
            client.searchAsync(req, RequestOptions.DEFAULT, new SearchActionListener<>(se)));
    }
    @AllArgsConstructor
    static class SearchActionListener<T> implements ActionListener<T> {
        SingleEmitter<T> se;

        @Override
        public void onResponse(T rspns) {
            se.onSuccess(rspns);
        }

        @Override
        public void onFailure(Exception excptn) {
            se.onError(excptn);
        }
    
    }
}
