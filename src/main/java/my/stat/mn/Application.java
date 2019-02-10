package my.stat.mn;

import io.micronaut.runtime.Micronaut;

/**
 * $ mn create-app my-stat-mn --features graal-native-image,mong-reactive,redis-lettuce,swagger-java,tracing-zipkin,micrometer-prometheus
 * $ cd my-stat-mn
 * $ mn create-controller StatController
 *
 * $ docker run --name mystat-pg -p 5432:5432 -e POSTGRES_USER=mystat -e POSTGRES_PASSWORD=pass -d postgres
 * create table USERS (
 *  user_id bigserial primary key,
 *  user_name text,
 *  user_handle text unique index, icon_url text,
 *  created_at timestamp default CURRENT_TIMESTAMP,
 *  updated_at timestamp default CURRENT_TIMESTAMP);
 * create unique index USERS_USER_HANDLE_INDEX on USERS (user_handle);
 * $ docker run -d --name mystat-minio -p 9000:9000 -e MINIO_ACCESS_KEY=mystat -e MINIO_SECRET_KEY=naokimystat minio/minio server /data
 * $ docker run --name mystat-redis -p 6379:6379 -d redis
 * $ docker run -d --name mystat-mongo -p 27117:27017 -d mongo
 * @author naoki
 */

public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class);
    }
}