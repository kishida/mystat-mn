package my.stat.mn.service;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.reactivestreams.client.FindPublisher;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import com.mongodb.reactivestreams.client.Success;
import io.micronaut.tracing.annotation.NewSpan;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Data;
import lombok.NoArgsConstructor;
import my.stat.mn.data.Status;
import my.stat.mn.data.User;
import my.stat.mn.messaging.StatProducer;
import org.bson.types.ObjectId;
import org.elasticsearch.action.index.IndexResponse;

/**
 *
 * @author naoki
 */
@Singleton
public class StatusService {

    @Inject
    MongoDatabase database;

    @Inject
    UserService userService;
    
    @Inject
    SearchService searchService;
    
    @Inject
    StatProducer statProducer;
    
    private MongoCollection<Status> getCollection() {
        return database.getCollection("status", Status.class);        
    }
    
    @NewSpan("mongo.status.insert")
    public Single<Success> insert(String handle, String text) {
        var coll = getCollection();
        Status status = Status.builder()
                .id(new ObjectId())
                .userHandle(handle)
                .text(text)
                .createdAt(LocalDateTime.now()).build();
        return Single.fromPublisher(coll.insertOne(status))
                .map(suc -> {
                    statProducer.sendStatus(status.getId().toString(), status);
                    return suc;
                });
    }
    
    @NewSpan("mongo.status.findById")
    public Maybe<TimelineElement> findById(String id) {
        FindPublisher<Status> sts = getCollection()
                .find(Filters.eq("_id", new ObjectId(id)));
        return Flowable.fromPublisher(sts)
                .firstElement()
                .map(s -> createElm(new HashMap<>(), s));
    }
    
    @NewSpan("mongo.status.timeline")
    public Flowable<TimelineElement> timeline() {
        return Flowable.fromPublisher(
            getCollection().find()
                    .sort(Sorts.descending("createdAt")))
                .to(this::bind);
    }
    
    @NewSpan("mongo.status.findByHandle")
    public Flowable<TimelineElement> findByHandle(String handle) {
        return Flowable.fromPublisher(
                getCollection()
                    .find(Filters.eq("userHandle", handle))
                    .sort(Sorts.descending("createdAt")))
                .to(this::bind);
    }
    
    public Flowable<TimelineElement> search(String keyword) {
        return searchService.search(keyword)
                .to(this::bind);
    }
    
    @Data
    @NoArgsConstructor
    public static class TimelineElement {
        User user;
        Status status;
        
        public String getUserHandle() {
            return user == null ? "not found" : user.getUserHandle();
        }
        
        public String getUserName() {
            return user == null ? "not found" : user.getUserName();
        }
    }
    
    private Flowable<TimelineElement> bind(Flowable<Status> statuses) {
        Map<String, User> userCache = new ConcurrentHashMap<>();
        return statuses.map(s -> createElm(userCache, s));
    }
    
    private TimelineElement createElm(Map<String, User> userCache, Status s) {
        var elm = new TimelineElement();
        elm.setStatus(s);
        elm.setUser(userCache
                .computeIfAbsent(s.getUserHandle(), 
                                 us -> userService.findByHandle(us).orElse(null)));
        return elm;
    }
    
}
