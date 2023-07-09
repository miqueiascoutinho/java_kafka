package br.com.miqueias.kafka.configuration;

import com.google.gson.Gson;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Map;

@Configuration
public class KafkaConfig {
    @Bean
    Gson createGson(){
        return new Gson();
    }

}
