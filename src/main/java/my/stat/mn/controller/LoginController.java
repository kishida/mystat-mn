package my.stat.mn.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.http.multipart.StreamingFileUpload;
import io.micronaut.views.View;
import io.minio.MinioClient;
import io.minio.messages.Bucket;
import java.io.InputStream;
import java.net.URI;
import javax.inject.Inject;
import my.stat.mn.data.User;
import my.stat.mn.repository.UserMapper;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 *
 * @author naoki
 */
@Controller("/")
@Produces(MediaType.TEXT_HTML)
public class LoginController {
    @Inject
    public MinioClient minio;
    
    @Inject
    public SqlSessionFactory sessionFactory;
    
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

    //@Post(value="/register", consumes = MediaType.APPLICATION_FORM_URLENCODED)
    @Post(value="/register", consumes = MediaType.MULTIPART_FORM_DATA)
    public HttpResponse doRegister(String handle, String name, CompletedFileUpload icon) throws Exception {
        try (var sess = sessionFactory.openSession()) {
            var mapper = sess.getMapper(UserMapper.class);
            mapper.insert(User.builder()
                              .userName(name)
                              .userHandle(handle).build());
        }
        
        var content = icon.getContentType().map(mt -> mt.toString()).orElse("image/jpg");
        minio.putObject("mystat", handle+"-icon", icon.getInputStream(), content);
        return HttpResponse.redirect(URI.create("/"));
    }
    
}
