package br.com.miqueias.kafka.representation;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderResponseRepresentation {
    private Integer codigoVenda;
    private String produto;
    private Integer quantidade;
    private BigDecimal valorTotal;
}
