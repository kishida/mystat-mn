package my.stat.mn;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoDatabase;
import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.Factory;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import javax.sql.DataSource;
import my.stat.mn.repository.UserMapper;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

/**
 *
 * @author naoki
 */
@Factory
public class ServersFactory {
    @Context
    public MinioClient minio() throws MinioException {
        return new MinioClient(
                "http://localhost:9000", "mystat", "naokimystat");
    }

    @Context
    public MongoDatabase mongoDb(MongoClient mongo) {
        return mongo.getDatabase("mystat");
    }
    
    @Context
    public SqlSessionFactory sessionFactory(DataSource ds) {
        var tran = new JdbcTransactionFactory();
        var env = new Environment("dev", tran, ds);
        var conf = new Configuration(env);
        conf.addMapper(UserMapper.class);
        conf.setMapUnderscoreToCamelCase(true);
        return new SqlSessionFactoryBuilder().build(conf);        
    }
}
