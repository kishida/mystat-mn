package my.stat.mn.service;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.Executors;
import my.stat.mn.ServersFactory;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.junit.Ignore;
import org.junit.Test;
import org.postgresql.ds.PGSimpleDataSource;

/**
 *
 * @author naoki
 */
public class StatusServiceTest {
    
    ServersFactory factory = null;
    ServersFactory factory() {
        if (factory == null) {
            factory = new ServersFactory();
        }
        return factory;
    }
    
    @Test
    public void findByIdTest() throws Exception {
        var id = "5c62f912cf82bf7e6a5d21ac";
        var status = mapper().findById(id);
        var exe = Executors.newSingleThreadExecutor();
        var s = status.subscribeOn(Schedulers.from(exe))
              .doFinally(() -> exe.shutdown())
              .blockingGet();
        System.out.println(s);
    }
    
    private UserService service() throws Exception{
        var service = new UserService();
        
        service.minio = factory().minio();
        
        var ds = new PGSimpleDataSource();
        ds.setDatabaseName("mystat");
        ds.setUser("mystat");
        ds.setPassword("pass");
        service.sessionFactory = factory().sessionFactory(ds);
        
        return service;
    }
    
    private StatusService mapper() throws Exception {
        var mapper = new StatusService();

        String url = "mongodb://localhost:27117";
        var registries = CodecRegistries.fromRegistries(
                MongoClients.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        var settings = MongoClientSettings.builder()
                .codecRegistry(registries)
                .applyConnectionString(new ConnectionString(url))
                .build();        
        MongoClient client = MongoClients.create(settings);
        
        var db = factory().mongoDb(client);;
        
        mapper.database = db;
        mapper.userService = service();
        return mapper;
    }
    
    @Test
    @Ignore
    public void timelineTest() throws Exception {
        var exe = Executors.newSingleThreadExecutor();
        var timeline = mapper().timeline();
        var result = timeline
                .subscribeOn(Schedulers.from(exe))
                .doFinally(() -> exe.shutdown())
                .blockingIterable();
                
        for (var f : result) {
            System.out.println(f);
        }
        //        .forEach(st -> System.out.println(st.getText())).dispose();
        
    }
    
    @Test
    public void findByHandleTest() throws Exception {
        var exe = Executors.newSingleThreadExecutor();
        var timeline = mapper().findByHandle("ab");
        var result = timeline
                .subscribeOn(Schedulers.from(exe))
                .doFinally(() -> exe.shutdown())
                .blockingIterable();
        for (var f: result) {
            System.out.println(f);
        }
    }
    
    @Test
    public void userTest() throws Exception {
        var mapper = mapper();
        var tl = mapper.timeline();
        for (var t : tl.blockingIterable()) {
            System.out.println(t);
        }
    }
}
