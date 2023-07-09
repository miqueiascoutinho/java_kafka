package br.com.miqueias.kafka.repository.producer;

import br.com.miqueias.kafka.Orders;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class KafkaProdutor {
    private final String topic;
    private final KafkaTemplate<String, Orders> template;

    @Autowired
    public KafkaProdutor(@Value("${topic.name.old}") String topic, KafkaTemplate<String, Orders> template) {
        this.template = template;
        this.topic = topic;
    }

    public void sendMessage(String key, Orders value) {

        ProducerRecord<String, Orders> record = new ProducerRecord<>(topic, null, key, value);
        CompletableFuture<SendResult<String, Orders>> result = template.send(record);

        result.whenComplete((finish, error) -> {
            if (Objects.isNull(error)) {
                log.info("..: sendMessage to topic: {}, key: {}, message: {}", topic, key, value);
            }
        });
    }
}
