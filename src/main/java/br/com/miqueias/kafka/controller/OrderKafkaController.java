package br.com.miqueias.kafka.controller;

import br.com.miqueias.kafka.domain.Order;
import br.com.miqueias.kafka.mapping.OrderMapping;
import br.com.miqueias.kafka.representation.OrderRepresentation;
import br.com.miqueias.kafka.service.OrdersService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/orders")
@AllArgsConstructor
public class OrderKafkaController {

    private final OrdersService ordersService;
    private final OrderMapping mapper;

    @PostMapping
    public ResponseEntity<OrderRepresentation> createOrders(@RequestBody OrderRepresentation input)  {
        Order order = mapper.toDomain(input);

        ordersService.orderV1(order);

        return ResponseEntity.ok(input);
    }
}
