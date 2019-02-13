package my.stat.mn.repository;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoDatabase;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.Executors;
import my.stat.mn.data.Status;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.junit.Test;

/**
 *
 * @author naoki
 */
public class StatusMapperTest {
    
    @Test
    public void findByIdTest() {
        var id = "5c62f912cf82bf7e6a5d21ac";
        Maybe<Status> status = mapper().findById(id);
        var exe = Executors.newSingleThreadExecutor();
        var s = status.subscribeOn(Schedulers.from(exe))
              .doFinally(() -> exe.shutdown())
              .blockingGet();
        System.out.println(s);
    }
    
    private StatusMapper mapper() {
        String url = "mongodb://localhost:27117";
        var registries = CodecRegistries.fromRegistries(
                MongoClients.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        var settings = MongoClientSettings.builder()
                .codecRegistry(registries)
                .applyConnectionString(new ConnectionString(url))
                .build();        
        MongoClient client = MongoClients.create(settings);
        var db = client.getDatabase("mystat");
        
        var mapper = new StatusMapper();
        mapper.database = db;
        return mapper;
    }
    
    @Test
    public void timelineTest() {
        System.out.println("timeline");
        var exe = Executors.newSingleThreadExecutor();
        Flowable<Status> timeline = mapper().timeline();
        var result = timeline
                .subscribeOn(Schedulers.from(exe))
                .doFinally(() -> exe.shutdown())
                .blockingIterable();
                
        for (var f : result) {
            System.out.println(f);
        }
        //        .forEach(st -> System.out.println(st.getText())).dispose();
        
    }
}
