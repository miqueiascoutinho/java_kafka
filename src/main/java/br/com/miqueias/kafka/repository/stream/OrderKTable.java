package br.com.miqueias.kafka.repository.stream;

import br.com.miqueias.kafka.domain.OrderResume;
import br.com.miqueias.kafka.domain.OrderResumeUser;
import br.com.miqueias.kafka.util.CustomSerdes;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class OrderKTable {

    @Value("${topic.name.join}") private String topicInput;
    @Value("${topic.name.resume-ktable}") private String outputTopic;
    @Autowired
    public void process(StreamsBuilder builder) {
        builder.table(topicInput, Materialized.<String, OrderResumeUser, KeyValueStore<Bytes, byte[]>>as("ktable-orders-resume")
                .withKeySerde(Serdes.String())
                .withValueSerde(CustomSerdes.ResumeUserJoin()))
                .filter((key, value) -> Objects.nonNull(value) && Objects.nonNull(value.getPedido()))
                .filter((key, value) -> (value.getPedido().getQuantidade() % 2) == 0)
                .toStream()
                .filter((key, value) -> Objects.nonNull(value))
                .peek((key, value) -> log.info(".. filtrando por quantidades pares -> {}", value.getPedido().getQuantidade()))
                .to(outputTopic, Produced.with(Serdes.String(), CustomSerdes.ResumeUserJoin()));
    }
}
