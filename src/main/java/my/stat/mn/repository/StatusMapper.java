package my.stat.mn.repository;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.reactivestreams.client.FindPublisher;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import com.mongodb.reactivestreams.client.Success;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.Single;
import java.time.LocalDateTime;
import javax.inject.Inject;
import javax.inject.Singleton;
import my.stat.mn.data.Status;
import org.bson.types.ObjectId;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 *
 * @author naoki
 */
@Singleton
public class StatusMapper {

    @Inject
    MongoDatabase database;
    
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
    
    public Maybe<Status> findById(String id) {
        FindPublisher<Status> s = getCollection()
                .find(Filters.eq("_id", new ObjectId(id)));
        return Flowable.fromPublisher(s)
                .firstElement();
    }
    
    public Flowable<Status> timeline() {
        return Flowable.fromPublisher(
            getCollection().find()
                    .sort(Sorts.descending("createdAt")));
    }
    
    public Flowable<Status> findByHandle(String handle) {
        return Flowable.fromPublisher(
                getCollection()
                    .find(Filters.eq("userHandle", handle)));
    }
}
