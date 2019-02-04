package my.stat.mn;

import io.micronaut.runtime.Micronaut;

/**
 * $ mn create-app my-stat-mn --features graal-native-image,mong-reactive,redis-lettuce,swagger-java,tracing-zipkin,micrometer-prometheus
 * $ cd my-stat-mn
 * $ mn create-controller StatController
 *
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