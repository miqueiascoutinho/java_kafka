package br.com.miqueias.kafka.repository.stream;

import br.com.miqueias.kafka.domain.Order;
import br.com.miqueias.kafka.domain.OrderAggregate;
import br.com.miqueias.kafka.util.CustomSerdes;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;

@Component
@Slf4j
public class OrderAggregation {

    @Value("${topic.name.aggregate-in}") private String inputTopic;
    @Value("${topic.name.aggregate-out}") private String outputTopic;

    @Autowired
    public void process(StreamsBuilder builder) {
        KStream<String, Order> stream = builder.stream(inputTopic, Consumed.with(Serdes.String(), CustomSerdes.Order()));

        KStream<String, BigDecimal> kStreamSoma = stream
                .groupByKey()
                .aggregate(() -> new BigDecimal("0.0"),
                        (key, order, total) -> total.add(order.getValorTotal()),
                        Materialized.with(Serdes.String(), CustomSerdes.BigDecimal()))
                .toStream()
                .peek((key, value) -> log.info("Fim da agregação -> key {} - total {}", key, value));

        KTable<String, Long> kTableCount = stream.groupByKey().count(Materialized.with(Serdes.String(), Serdes.Long()));

        ValueJoiner<BigDecimal,Long, OrderAggregate> valueJoin = ((soma, qtde) -> OrderAggregate.builder()
                .quantidade(qtde)
                .total(soma)
                .build());

        KStream<String, OrderAggregate> orderAgg = kStreamSoma
                .join(
                kTableCount,
                valueJoin
        );

        orderAgg.peek((key, value) -> {
            value.setId(key);
            log.info(".. fim do join quantidade X total -> key {} -> value {}", key, value);
        }).to(outputTopic, Produced.with(Serdes.String(), CustomSerdes.OrderAggregate()));
    }
}
