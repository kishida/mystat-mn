package my.stat.mn;

import io.micronaut.runtime.Micronaut;

/**
 * $ mn create-app my-stat-mn --features graal-native-image,mongo-reactive,redis-lettuce,swagger-java,tracing-zipkin,micrometer-prometheus
 * $ cd my-stat-mn
 * $ mn create-controller StatController
 *
 * $ docker run -d --name mystat-pg -p 5432:5432 -e POSTGRES_USER=mystat -e POSTGRES_PASSWORD=pass postgres
 * $ docker exec -it mystat-pg psql -U mystat
 * create table USERS (
 *  user_id bigserial primary key,
 *  user_name text,
 *  user_handle text, icon_url text,
 *  created_at timestamp default CURRENT_TIMESTAMP,
 *  updated_at timestamp default CURRENT_TIMESTAMP);
 * create unique index USERS_USER_HANDLE_INDEX on USERS (user_handle);
 * 
 * $ docker run -d --name mystat-minio -p 9000:9000 -e MINIO_ACCESS_KEY=mystat -e MINIO_SECRET_KEY=naokimystat minio/minio server /data
 * $ docker run -d --name mystat-redis -p 6379:6379 redis
 * $ docker exec -it mystat-redis redis-cli
 * > keys user*
 * > get user:aa
 * 
 * $ docker run -d --name mystat-zipkin -p 9411:9411 openzipkin/zipkin
 * $ docker run -d --name mystat-mongo -p 27117:27017 mongo
 * $ docker run -d --name mystat-prom -p 9090:9090 -v /c/Users/naoki/Documents/NetBeansProjects/my-stat-mn:/prom-data prom/prometheus --config.file=/prom-data/prometheus.yml
 * @author naoki
 */

public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class);
    }
}