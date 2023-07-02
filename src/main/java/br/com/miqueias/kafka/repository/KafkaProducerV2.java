package br.com.miqueias.kafka.repository;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerV2 {
    private final KafkaTemplate<String, String> template;

    private final Gson gson;

    @Value("${topic.name.new}")
    private String topic;

    public <T> void sendMessage(String key, T value){
        String json = gson.toJson(value);
        log.info(".. Send message to topic {}, witk key: {}, message: {}", topic, key, json);
        CompletableFuture<SendResult<String, String>> send = template.send(topic, null, key, json);

        send.whenComplete((success, error) -> {
            if (Objects.isNull(error)) {
                log.info(".. Send message to topic {} with success", topic);
            } else {
                log.error(".. Error to send message to topic {} error {}", topic, error.getMessage());
            }
        });
    }
}
