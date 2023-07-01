package br.com.miqueias.kafka.repository;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class KafkaProducerV2 {
    private final KafkaTemplate<String, String> template;
    private final String topic;

    @Autowired
    public KafkaProducerV2(KafkaTemplate<String, String> template, @Value("${topic.name.new}") String topic){
        this.template = template;
        this.topic = topic;
    }

    public <T> void sendMessage(String key, T value){
        Gson gson = new Gson();
        String json = gson.toJson(value);
        log.info(".. Send message to topic {}, witk key: {}, message: {}", topic, key, json);
        CompletableFuture<SendResult<String, String>> send = template.send(topic, null, key, json);

        send.whenComplete((suss, error) -> {
            if (Objects.isNull(error)) {
                log.info("Finalizado com sucesso ..");
            } else {
                log.error(".. Error to send message to topic {} error {}", topic, error.getMessage());
            }
        });
    }
}
