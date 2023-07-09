package br.com.miqueias.kafka.controller;

import br.com.miqueias.kafka.domain.Order;
import br.com.miqueias.kafka.domain.OrderResume;
import br.com.miqueias.kafka.mapper.OrderMapper;
import br.com.miqueias.kafka.representation.OrderRepresentation;
import br.com.miqueias.kafka.representation.OrderResponseRepresentation;
import br.com.miqueias.kafka.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderKafkaController {

    private final OrdersService ordersService;
    private final OrderMapper mapper;

    @PostMapping
    public ResponseEntity<OrderResponseRepresentation> createOrders(@RequestBody OrderRepresentation input)  {
        Order order = mapper.toDomain(input);

        OrderResume orderResume = ordersService.orderV1(order);

        return ResponseEntity.ok(mapper.toRepresentation(orderResume));
    }
}
