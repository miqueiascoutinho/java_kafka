package br.com.miqueias.kafka.representation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderRepresentation {
    private String produto;
    private int quantidade;
    private BigDecimal valor;
    private UsuarioRepresentation comprador;
    private UsuarioRepresentation vendedor;
}
