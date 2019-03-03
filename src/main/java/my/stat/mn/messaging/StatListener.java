package my.stat.mn.messaging;

import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.reactivex.Single;
import javax.inject.Inject;
import my.stat.mn.data.Status;
import my.stat.mn.service.SearchService;
import org.elasticsearch.action.index.IndexResponse;

/**
 *
 * @author naoki
 */
@KafkaListener
public class StatListener {
    @Inject
    SearchService searchService;
    
    @Topic("mystat-status")
    public Single<IndexResponse> createStatusIndex(@KafkaKey String id, Status stat) {
        return searchService.createIndex(stat);
    }
}
