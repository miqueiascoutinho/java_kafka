package br.com.miqueias.kafka.mapper;

import br.com.miqueias.kafka.domain.Order;
import br.com.miqueias.kafka.representation.OrderRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.math.BigDecimal;

@Mapper(componentModel = "spring", imports = {BigDecimal.class})
public interface OrderMapper {
    @Mapping(source = "rep.valor", target = "valorUnitario")
    @Mapping(expression = "java(rep.getValor().multiply(BigDecimal.valueOf(rep.getQuantidade())))", target = "valorTotal")
    Order toDomain(OrderRepresentation rep);

}
