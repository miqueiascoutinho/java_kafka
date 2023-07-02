package br.com.miqueias.kafka.repository.consumer;

import br.com.miqueias.kafka.domain.Order;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderConsumer {

    private final Gson gson;
    @KafkaListener(topics = "${topic.name.new}", groupId = "estoque")
    public void processMessage(ConsumerRecord<String, String> record){
        log.info("Kafka Listener read message from topic {}, partition {}, offset {} with key {} and message {}", record.topic(), record.partition(), record.offset(),
                record.key(), record.value());
        String message = record.value();
        String json = gson.toJson(message);
        Order order = gson.fromJson(message, Order.class);

        log.info("Order consumer {}", order);
    }
}
