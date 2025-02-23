package br.com.alterdata.vendas.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class ProdutoCriacaoRequest {
	
	private Long id;
    private String nome;
    private String descricao;
    private String referencia;
    private BigDecimal valorUnitario;
    private Long idCategoria;

}
