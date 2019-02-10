package my.stat.mn.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.views.View;
import io.minio.MinioClient;
import java.net.URI;
import java.util.Optional;
import javax.inject.Inject;
import my.stat.mn.data.User;
import my.stat.mn.repository.UserMapper;
import my.stat.mn.service.UserService;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 *
 * @author naoki
 */
@Controller("/")
@Produces(MediaType.TEXT_HTML)
public class LoginController {
    @Inject
    public UserService userService;
    
    @View("login")
    @Get("/login")
    public HttpResponse login() {
        return HttpResponse.ok();
    }
    
    @Post(value="/login", consumes = MediaType.APPLICATION_FORM_URLENCODED)
    public HttpResponse doLogin(String handle) {
        Optional<User> user = userService.findByHandle(handle);
        return user.map(u -> HttpResponse.redirect(URI.create("/")))
                .orElseGet(() -> HttpResponse.notFound());
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
        return HttpResponse.redirect(URI.create("/"));
    }
}
