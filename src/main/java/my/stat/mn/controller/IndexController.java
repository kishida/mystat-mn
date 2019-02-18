package my.stat.mn.controller;

import my.stat.mn.data.Status;
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
import my.stat.mn.service.StatusService;
import my.stat.mn.service.UserService;

/**
 *
 * @author naoki
 */
@Controller(value="/")
@Produces(MediaType.TEXT_HTML)
@Secured(SecurityRule.IS_ANONYMOUS)
public class IndexController {
    
    @Inject
    StatusService statusService;
    
    @Inject
    UserService userService;
    
    @Get(uri="/")
    public Single<ModelAndView> index(@Nullable Principal principal) { // Optinalは使えない
        if (principal == null) {
            return Single.just(new ModelAndView("index", null));
        }
        
        return statusService.timeline()
                .toList()
                .map(statuses -> {
                    Map<String, Object> context = new HashMap<>();
                    context.put("user", userService.findByHandle(principal.getName()).orElse(null));
                    context.put("statuses", statuses);
                    return new ModelAndView("timeline", context);
                });
    }
    
    @Post(uri="/", consumes = MediaType.APPLICATION_FORM_URLENCODED)
    public Single<ModelAndView> indexSubmit(@Nullable Principal principal, String text) {
        if (principal == null) {
            return index(principal);
        }
        return statusService.insert(principal.getName(), text)
                     .flatMap(s -> index(principal));
    }
}
