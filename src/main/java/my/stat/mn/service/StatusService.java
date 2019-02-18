package my.stat.mn.service;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.reactivestreams.client.FindPublisher;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import com.mongodb.reactivestreams.client.Success;
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
import org.bson.types.ObjectId;

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
    
    private MongoCollection<Status> getCollection() {
        return database.getCollection("status", Status.class);        
    }
    
    public Single<Success> insert(String handle, String text) {
        var coll = getCollection();
        return Single.fromPublisher(coll.insertOne(
                        Status.builder().userHandle(handle)
                                        .text(text)
                                        .createdAt(LocalDateTime.now()).build()));
    }
    
    public Maybe<TimelineElement> findById(String id) {
        FindPublisher<Status> sts = getCollection()
                .find(Filters.eq("_id", new ObjectId(id)));
        return Flowable.fromPublisher(sts)
                .firstElement()
                .map(s -> createElm(new HashMap<>(), s));
    }
    
    public Flowable<TimelineElement> timeline() {
        return Flowable.fromPublisher(
            getCollection().find()
                    .sort(Sorts.descending("createdAt")))
                .to(this::bind);
    }
    
    public Flowable<TimelineElement> findByHandle(String handle) {
        return Flowable.fromPublisher(
                getCollection()
                    .find(Filters.eq("userHandle", handle)))
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
