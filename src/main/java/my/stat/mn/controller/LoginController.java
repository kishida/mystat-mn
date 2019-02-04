package my.stat.mn.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.views.View;

/**
 *
 * @author naoki
 */
@Controller("/")
@Produces(MediaType.TEXT_HTML)
public class LoginController {
    @View("login")
    @Get("/login")
    public HttpResponse login() {
        return HttpResponse.ok();
    }

    @View("register")
    @Get("/register")
    public HttpResponse register() {
        return HttpResponse.ok();
    }

}
