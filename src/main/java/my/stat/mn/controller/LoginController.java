package my.stat.mn.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.View;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;
import my.stat.mn.data.User;
import my.stat.mn.service.UserService;

/**
 *
 * @author naoki
 */
@Controller("/")
@Produces(MediaType.TEXT_HTML)
@Secured(SecurityRule.IS_ANONYMOUS)
public class LoginController {
    @Inject
    public UserService userService;
    
    @View("login")
    @Get("/login/auth")
    public HttpResponse login() {
        return HttpResponse.ok(new HashMap(Map.of("error", false)));
    }
    
    @View("login")
    @Get("/login/authFailed")
    public HttpResponse loginFaild() {
        return HttpResponse.ok(new HashMap(Map.of("error", true)));
    }

    @View("register")
    @Get("/register")
    public HttpResponse register() {
        return HttpResponse.ok();
    }

    @Post(value="/register", consumes = MediaType.MULTIPART_FORM_DATA)
    public HttpResponse doRegister(String handle, String name, CompletedFileUpload icon) throws Exception {
        var user = User.builder().userHandle(handle).userName(name).build();
        userService.register(user, icon);
        return HttpResponse.redirect(URI.create("/login/auth"));
    }
}
