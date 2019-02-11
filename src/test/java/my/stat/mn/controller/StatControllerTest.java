package my.stat.mn.controller;

import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;

public class StatControllerTest {

    @Test
    @Ignore // todo auth
    public void testStat() throws Exception {
        try(EmbeddedServer server = ApplicationContext.run(EmbeddedServer.class);
            RxHttpClient client = server.getApplicationContext().createBean(RxHttpClient.class, server.getURL()))
        {
            assertEquals(HttpStatus.OK, client.toBlocking().exchange("/stat/foo").status());
        }
    }
}
