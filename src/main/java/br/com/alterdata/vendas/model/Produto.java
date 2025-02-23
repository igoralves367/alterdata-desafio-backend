package br.com.alterdata.vendas.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sun.istack.NotNull;

import br.com.alterdata.vendas.dto.ProdutoCriacaoRequest;
import br.com.alterdata.vendas.dto.ProdutoEdicaoRequest;
import br.com.alterdata.vendas.service.CategoriaService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "produtos")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Produto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String nome;

	@NotNull
	private String descricao;

	@NotNull
	private String referencia;

	@NotNull
	@Column(name = "valor_unitario")
	private BigDecimal valorUnitario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "categoria_id")
	private Categoria categoria;

	public Produto(ProdutoCriacaoRequest produtoDTO, CategoriaService categoriaService) {
		this.nome = produtoDTO.getNome();
		this.descricao = produtoDTO.getDescricao();
		this.referencia = produtoDTO.getReferencia();
		this.valorUnitario = produtoDTO.getValorUnitario();
		this.categoria = categoriaService.buscaCategoriaPorId(produtoDTO.getIdCategoria());
	}

	public void edita(ProdutoEdicaoRequest produtoEdicaoRequest, CategoriaService categoriaService) {
		this.nome = produtoEdicaoRequest.getNome();
		this.descricao = produtoEdicaoRequest.getDescricao();
		this.referencia = produtoEdicaoRequest.getReferencia();
		this.valorUnitario = produtoEdicaoRequest.getValorUnitario();
		this.categoria = categoriaService.buscaCategoriaPorId(produtoEdicaoRequest.getIdCategoria());
	}
}
