package br.com.alterdata.vendas.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import br.com.alterdata.vendas.model.Produto;
import lombok.Getter;

@Getter
public class ProdutoResponse {
	
	private Long id;
    private String nome;
    private String descricao;
    private String referencia;
    private BigDecimal valorUnitario;
    
    public ProdutoResponse(Produto produto) {
    	this.id = produto.getId();
		this.nome = produto.getNome();
		this.descricao = produto.getDescricao();
		this.referencia = produto.getReferencia();
		this.valorUnitario = produto.getValorUnitario();
	}

	public static List<ProdutoResponse> converte(List<Produto> produtos) {
		return produtos.stream()
				.map(ProdutoResponse::new)
				.collect(Collectors.toList());
	}
}
