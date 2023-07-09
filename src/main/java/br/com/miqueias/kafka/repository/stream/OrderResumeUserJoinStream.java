package br.com.miqueias.kafka.repository.stream;

import br.com.miqueias.kafka.domain.OrderResume;
import br.com.miqueias.kafka.domain.OrderResumeUser;
import br.com.miqueias.kafka.domain.Usuario;
import br.com.miqueias.kafka.util.CustomSerdes;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Slf4j
public class OrderResumeUserJoinStream {

    @Value("${topic.name.resume}") private String topicResume;
    @Value("${topic.name.users}") private String topicUsuers;
    @Value("${topic.name.join}") private String topicJoin;

    @Autowired
    public void process(StreamsBuilder builder){
        KStream<String, OrderResume> resumeStream = builder.stream(topicResume, Consumed.with(Serdes.String(), CustomSerdes.OrderResume()));
        KStream<String, Usuario> usersStream = builder.stream(topicUsuers, Consumed.with(Serdes.String(), CustomSerdes.Usuario()));

    ValueJoiner<OrderResume, Usuario, OrderResumeUser> orderUserJoiner = (resumeValue, orderValue) -> OrderResumeUser.builder()
            .pedido(resumeValue)
            .comprador(orderValue)
       .build();

        KStream<String, OrderResumeUser> joinStream = resumeStream.join(usersStream, orderUserJoiner, JoinWindows.of(Duration.ofMinutes(10)),
                StreamJoined.with(Serdes.String(), CustomSerdes.OrderResume(), CustomSerdes.Usuario()));

        joinStream.peek((key, value) -> log.info("..: Join realizado para a key {} e valores {}", key, value.toString()))
               .to(topicJoin, Produced.with(Serdes.String(), CustomSerdes.ResumeUserJoin()));
    }
}
