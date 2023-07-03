package br.com.miqueias.kafka.util;

import br.com.miqueias.kafka.domain.Order;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

@NoArgsConstructor
public class CustomSerdes {

    public static Serde<Order> Order(){
        JsonSerializer<Order> serializer = new JsonSerializer<>();
        JsonDeserializer<Order> deserializer = new JsonDeserializer<>(Order.class);

        return Serdes.serdeFrom(serializer, deserializer);
    }

}
