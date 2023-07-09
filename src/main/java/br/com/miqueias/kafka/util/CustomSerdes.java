package br.com.miqueias.kafka.util;

import br.com.miqueias.kafka.domain.*;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

import java.math.BigDecimal;

@NoArgsConstructor
public class CustomSerdes {

    public static Serde<Order> Order(){
        JsonSerializer<Order> serializer = new JsonSerializer<>();
        JsonDeserializer<Order> deserializer = new JsonDeserializer<>(Order.class);

        return Serdes.serdeFrom(serializer, deserializer);
    }

    public static Serde<OrderResume> OrderResume(){
        JsonSerializer<OrderResume> serializer = new JsonSerializer<>();
        JsonDeserializer<OrderResume> deserializer = new JsonDeserializer<>(OrderResume.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }

    public static Serde<Usuario> Usuario(){
        JsonSerializer<Usuario> serializer = new JsonSerializer<>();
        JsonDeserializer<Usuario> deserializer = new JsonDeserializer<>(Usuario.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }

    public static Serde<OrderResumeUser> ResumeUserJoin() {
        JsonSerializer<OrderResumeUser> serializer = new JsonSerializer<>();
        JsonDeserializer<OrderResumeUser> deserializer = new JsonDeserializer<>(OrderResumeUser.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }

    public static Serde<BigDecimal> BigDecimal() {
        JsonSerializer<BigDecimal> serializer = new JsonSerializer<>();
        JsonDeserializer<BigDecimal> deserializer = new JsonDeserializer<>(BigDecimal.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }

    public static Serde<OrderAggregate> OrderAggregate() {
        JsonSerializer<OrderAggregate> serializer = new JsonSerializer<>();
        JsonDeserializer<OrderAggregate> deserializer = new JsonDeserializer<>(OrderAggregate.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }
}
