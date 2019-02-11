package my.stat.mn.controller;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import java.security.Principal;
import java.util.Optional;
import javax.annotation.Nullable;
/**
 *
 * @author naoki
 */
@Controller("/")
@Secured(SecurityRule.IS_ANONYMOUS)
@Produces(MediaType.TEXT_PLAIN)
public class ErrorController {
    @Get("/unauthorized")
    public String unauthorized(@Nullable Principal principal) {
        var popt = Optional.ofNullable(principal);
        return "unauthorized " + popt.map(p -> p.getName()).orElse("no user");
    }
    
    @Get("/forbidden")
    public String forbidden() {
        return "forbidden";
    }
    
    @Error(status = HttpStatus.NOT_FOUND, global = true)
    public String notFound(HttpRequest req) {// security onだとダメ
        return req.getUri() + " not found";
    }
}
