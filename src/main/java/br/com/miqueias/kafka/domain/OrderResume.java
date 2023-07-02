package br.com.miqueias.kafka.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderResume {
    private Integer codigoVenda;
    private String produto;
    private Integer quantidade;
    private BigDecimal valorTotal;
}
