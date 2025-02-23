package br.com.alterdata.vendas.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProdutoCriacaoRequest {
	
	private Long id;
    private String nome;
    private String descricao;
    private String referencia;
    private BigDecimal valorUnitario;
    private Long idCategoria;

}
