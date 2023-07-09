package br.com.miqueias.kafka.repository.stream;

import br.com.miqueias.kafka.domain.Order;
import br.com.miqueias.kafka.util.CustomSerdes;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderStream {

    @Value("${topic.name.new}")
    private String topic;
    @Value("${topic.name.aggregate-in}") private String aggregateTopic;
    @Value("${topic.name.kafka-streams}") private String streamsTopics;

    @Autowired
    public void process(StreamsBuilder kStreamsBuilder) {
        final Serde<String> stringSerde = Serdes.String();

        KStream<String, Order> stream = kStreamsBuilder.stream(topic, Consumed.with(stringSerde, CustomSerdes.Order()));

        //greater than 500
        stream
                .peek((key, value) -> log.info("..: Kafka Streams - Pedidos maiores que 500 -> Analisando key: {} - order: {}", key, value.toString()))
                .filter((key, order) -> order.getQuantidade() > 500)
                .peek((key, value) -> log.info("..: Kafka Streams - output: Pedidos maiores que 500 -> enviando para o tópico [ecommerce-orders-greater-than-500] -  key: {} - order: {}", key, value.toString()))
                .to("ecommerce-orders-greater-than-500", Produced.with(stringSerde, CustomSerdes.Order()));

        stream.filter((key, order) -> order.getQuantidade() < 100)
                .peek((key, value) -> log.info("..: Kafka Streams - output: Pedidos menores que 100 -> enviando para o tópico [ecommerce-orders-less-than-100] -  key: {} - order: {}", key, value.toString()))
                .to("ecommerce-orders-less-than-100");

        duplicateMessage(stream, aggregateTopic, Serdes.String(), CustomSerdes.Order());
        duplicateMessage(stream, streamsTopics, Serdes.String(), CustomSerdes.Order());
        duplicateMessage(stream, streamsTopics + "-2", Serdes.String(), CustomSerdes.Order());
    }

    private<K,V> void duplicateMessage(KStream<K,V> stream, String topic, Serde<K> serdesKey, Serde<V> serdesValue) {
        stream.peek((key, value) -> log.info(".. DuplicateMessage -> Duplicando mensagem no tópico [{}] - key: {} - value: {}", topic, key, value.toString()))
                .to(topic, Produced.with(serdesKey, serdesValue));

    }
}
