package br.com.miqueias.kafka.repository.stream;

import br.com.miqueias.kafka.domain.Order;
import br.com.miqueias.kafka.domain.OrderResume;
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
public class OrderResumeStream {

    @Value("${topic.name.new}")  private String topicInput;
    @Value("${topic.name.resume}") private String topicOutput;

    @Autowired
    public void process(StreamsBuilder builder) {
        Serde<String> stringSerDes = Serdes.String();
        Serde<Order> orderSerDes = CustomSerdes.Order();

        KStream<String, Order> stream = builder.stream(topicInput, Consumed.with(stringSerDes, orderSerDes));

        stream
            .peek((key, order) -> log.info(".. OrderResumeStream process key {} and order {}", key, order.toString()))
            .mapValues((key, order) ->
                OrderResume.builder()
                   .valorTotal(order.getValorTotal())
                   .produto(order.getProduto())
                   .codigoVenda(order.getId())
                   .quantidade(order.getQuantidade())
                   .build()
        ).to(topicOutput, Produced.with(stringSerDes, CustomSerdes.OrderResume()));
    }
}
