package my.stat.mn.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.View;

@Controller("/")
@Produces(MediaType.TEXT_HTML)
@Secured(SecurityRule.IS_AUTHENTICATED)
public class StatController {

    @View("stats")
    @Get("/user/{user}")
    public HttpStatus users(String user) {
        return HttpStatus.OK;
    }
    
    @View("stat")
    @Get("/stat/{id}")
    public HttpStatus stat(String id) {
        return HttpStatus.OK;
    }
}