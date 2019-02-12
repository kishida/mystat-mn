package my.stat.mn.repository;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import com.mongodb.reactivestreams.client.Success;
import io.reactivex.Single;
import java.time.LocalDateTime;
import javax.inject.Inject;
import javax.inject.Singleton;
import my.stat.mn.data.Status;

/**
 *
 * @author naoki
 */
@Singleton
public class StatusMapper {

    @Inject
    MongoDatabase database;
    
    private MongoCollection<Status> getMapper() {
        return database.getCollection("status", Status.class);        
    }
    
    public Single<Success> insert(String handle, String text) {
        MongoCollection<Status> coll = getMapper();
        return Single.fromPublisher(coll.insertOne(
                        Status.builder().userHandle(handle)
                                        .text(text)
                                        .createdAt(LocalDateTime.now()).build()));
    }
}
