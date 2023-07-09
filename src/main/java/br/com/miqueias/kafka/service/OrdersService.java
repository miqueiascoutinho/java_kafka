package br.com.miqueias.kafka.service;

import br.com.miqueias.kafka.domain.Order;
import br.com.miqueias.kafka.domain.OrderResume;
import br.com.miqueias.kafka.repository.producer.KafkaProducerV2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrdersService {

    private Integer idVenda = 1010;

    @Autowired
    KafkaProducerV2 orderProducer;
    public OrderResume orderV1(Order order){
        order.setId(novaVenda());
      log.info(".. OrderService init with: {}", order);

        orderProducer.sendMessage(order.getId().toString(), order);

        return OrderResume.builder()
                .codigoVenda(order.getId())
                .produto(order.getProduto())
                .quantidade(order.getQuantidade())
                .valorTotal(order.getValorTotal())
                .build();
    }

    private Integer novaVenda(){
        if(idVenda >= 1020) idVenda = 1010;
        return idVenda++;
    }
}
