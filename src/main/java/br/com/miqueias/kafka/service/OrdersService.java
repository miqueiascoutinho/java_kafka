package br.com.miqueias.kafka.service;

import br.com.miqueias.kafka.domain.Order;
import br.com.miqueias.kafka.repository.KafkaProducerV2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class OrdersService {

    @Autowired
    KafkaProducerV2 orderProducer;
    public void orderV1(Order order){
      log.info(".. OrderService init with: {}", order);

        orderProducer.sendMessage(order.getComprador().getEmail(), order);
    }
}
