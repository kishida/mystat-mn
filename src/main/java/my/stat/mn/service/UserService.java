package my.stat.mn.service;

import io.micronaut.cache.annotation.CacheInvalidate;
import io.micronaut.cache.annotation.Cacheable;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.minio.MinioClient;
import java.util.Optional;
import java.util.function.Function;
import javax.inject.Inject;
import javax.inject.Singleton;
import my.stat.mn.data.User;
import my.stat.mn.repository.UserMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 *
 * @author naoki
 */
@Singleton
public class UserService {
    @Inject
    MinioClient minio;
    
    @Inject
    SqlSessionFactory sessionFactory;

    @Cacheable("user")
    public Optional<User> findByHandle(String handle) {
        return withMapper(mapper -> {
            return mapper.findByHandle(handle);
        });
    }
    
    @CacheInvalidate(value = "user", parameters = "handle")// since it can't specify like $u.userHandle. need param for the key
    public void register(String handle, User u, CompletedFileUpload icon) {
        withMapper(mapper -> {
            mapper.insert(u);
            var content = icon.getContentType().map(mt -> mt.toString()).orElse("image/jpg");
            try {
                minio.putObject("mystat", u.getUserHandle()+"-icon",
                        icon.getInputStream(), content);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            return null;
        });
    }
    
    public String iconUrl(String handle) {
        try {
            return minio.presignedGetObject("mystat", handle + "-icon");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private <R> R withMapper(Function<UserMapper, R> func) {
        try (var sess = sessionFactory.openSession()) {
            var mapper = sess.getMapper(UserMapper.class);
            var result = func.apply(mapper);
            sess.commit();
            return result;
        }
    }
}
