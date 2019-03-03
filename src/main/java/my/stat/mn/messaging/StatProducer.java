package my.stat.mn.messaging;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;
import my.stat.mn.data.Status;

/**
 *
 * @author naoki
 */
@KafkaClient
public interface StatProducer {
    @Topic("mystat-status")
    void sendStatus(@KafkaKey String id, Status stat);
}
