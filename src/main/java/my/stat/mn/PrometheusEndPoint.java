package my.stat.mn;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.micronaut.management.endpoint.annotation.Endpoint;
import io.micronaut.management.endpoint.annotation.Read;
import static io.reactivex.schedulers.Schedulers.io;
import javax.inject.Inject;

/**
 *
 * @author naoki
 */
@Endpoint(id = "prometheus", value="/prometheus", defaultEnabled=true, defaultSensitive=false)
public class PrometheusEndPoint {
    @Inject
    private PrometheusMeterRegistry prometheus;

    @Read
    public String scrape() {
        return prometheus.scrape();
    }
}
