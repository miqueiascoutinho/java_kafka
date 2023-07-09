package br.com.miqueias.kafka.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderAggregate {
    private String id;
    private Long quantidade;
    private BigDecimal total;
}
