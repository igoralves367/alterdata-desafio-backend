package br.com.alterdata.vendas.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoDTO {
	
	private Long id;
    private String nome;
    private String descricao;
    private String referencia;
    private BigDecimal valorUnitario;

}
