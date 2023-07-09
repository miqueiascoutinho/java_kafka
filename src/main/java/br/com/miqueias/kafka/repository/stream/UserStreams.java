package br.com.miqueias.kafka.repository.stream;

import br.com.miqueias.kafka.domain.Order;
import br.com.miqueias.kafka.domain.Usuario;
import br.com.miqueias.kafka.util.CustomSerdes;
import lombok.extern.slf4j.Slf4j;
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
public class UserStreams {
    @Value(("${topic.name.users}")) private String outputTopic;
    @Value("${topic.name.new}") private String inputTopic;

    @Autowired
    public void process(StreamsBuilder builder) throws InterruptedException {
        KStream<String, Order> stream = builder.stream(inputTopic, Consumed.with(Serdes.String(), CustomSerdes.Order()));
        stream.mapValues((key, value) -> value.getComprador())
                .peek((key , value) -> log.info(".. CompradorStream process key {} send to topic {}", key, outputTopic))
                .to(outputTopic, Produced.with(Serdes.String(), CustomSerdes.Usuario()));
    }

}
