package my.stat.mn;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import javax.inject.Singleton;
import javax.sql.DataSource;
import my.stat.mn.repository.UserMapper;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.postgresql.ds.PGSimpleDataSource;

/**
 *
 * @author naoki
 */
@Factory
public class ServersFactory {
    @Bean
    @Singleton
    public MinioClient minio() throws MinioException {
        return new MinioClient(
                "http://localhost:9000", "mystat", "naokimystat");
    }
    
    @Bean
    @Singleton
    public DataSource dataSource() {
        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setDatabaseName("mystat");
        ds.setServerName("localhost");
        ds.setUser("mystat");
        ds.setPassword("pass");
        return ds;
    }
    
    @Bean
    @Singleton
    public SqlSessionFactory sessionFactory(DataSource ds) {
        var tran = new JdbcTransactionFactory();
        var env = new Environment("dev", tran, ds);
        var conf = new Configuration(env);
        conf.addMapper(UserMapper.class);
        conf.setMapUnderscoreToCamelCase(true);
        return new SqlSessionFactoryBuilder().build(conf);        
    }
}
