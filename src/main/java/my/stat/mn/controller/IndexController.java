package my.stat.mn.controller;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.ModelAndView;
import io.reactivex.Single;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import javax.inject.Inject;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

/**
 *
 * @author naoki
 */
@Controller(value="/")
@Produces(MediaType.TEXT_HTML)
@Secured(SecurityRule.IS_ANONYMOUS)
public class IndexController {
    
    @Get(uri="/")
    public ModelAndView index(@Nullable Principal principal) { // Optinalは使えない
        if (principal == null) {
            return new ModelAndView("index", null);
        }
        Map<String, String> context = new HashMap<>();
        context.put("name", principal.getName());
        return new ModelAndView("timeline", context);
    }
    
    @Inject
    MongoClient mongo;
    
    @Data
    @Builder
    public static class Status {
        ObjectId oid;
        String userHandle;
        String text;
        LocalDateTime createdAt;
    }
    
    @Post(uri="/", consumes = MediaType.APPLICATION_FORM_URLENCODED)
    public Single<ModelAndView> indexSubmit(@Nullable Principal principal, String text) {
        if (principal == null) {
            return Single.just(index(principal));
        }
        MongoDatabase db = mongo.getDatabase("mystat");
        MongoCollection<Status> coll = db.getCollection("status", Status.class);
        return Single.fromPublisher(coll.insertOne(
                        Status.builder().userHandle(principal.getName())
                                        .text(text)
                                        .createdAt(LocalDateTime.now()).build()))
                     .map(s -> index(principal));
    }
}
