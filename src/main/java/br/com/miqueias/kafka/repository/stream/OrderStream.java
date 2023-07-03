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

    @Autowired
    public void process(StreamsBuilder kStreamsBuilder) {
        final Serde<String> stringSerde = Serdes.String();

        KStream<String, Order> stream = kStreamsBuilder.stream(topic, Consumed.with(stringSerde, CustomSerdes.Order()));

        //greater than 500
        stream.filter((key, order) -> order.getQuantidade() > 500).to("ecommerce-orders-greater-than-500", Produced.with(stringSerde, CustomSerdes.Order()));

        stream.filter((key, order) -> order.getQuantidade() < 100).to("ecommerce-orders-less-than-100");
    }
}
