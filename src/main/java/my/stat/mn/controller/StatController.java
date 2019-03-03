package my.stat.mn.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.View;
import io.reactivex.Maybe;
import io.reactivex.Single;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import my.stat.mn.service.StatusService;

@Controller("/")
@Produces(MediaType.TEXT_HTML)
@Secured(SecurityRule.IS_ANONYMOUS) // IS_AUTHENTICATEDはバグってる
public class StatController {

    @Inject
    StatusService statusService;
    
    @View("stats")
    @Get("/user/{user}")
    public Single<Map<String, Object>> user(String user) {
        return statusService.findByHandle(user)
                .toList()
                .map(tles -> new HashMap<>(Map.of("statuses", tles)));
    }
    
    @View("stat")
    @Get("/stat/{id}")
    public Maybe<Map<String, Object>> stat(String id) {
        return statusService.findById(id)
                .map(tle -> new HashMap<>(Map.of("status", tle)));
    }
    
    @View("stats")
    @Get(value="/search")
    public Single<Map<String, Object>> search(@QueryValue String keyword) {
        return statusService.search(keyword)
                .toList()
                .map(tles -> new HashMap<>(Map.of("statuses", tles)));
    }

}