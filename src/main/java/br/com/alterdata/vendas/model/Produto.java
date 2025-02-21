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

import org.modelmapper.ModelMapper;

import com.sun.istack.NotNull;

import br.com.alterdata.vendas.dto.ProdutoDTO;
import br.com.alterdata.vendas.service.CategoriaService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "produtos")
@Data
@AllArgsConstructor
@NoArgsConstructor
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

	public void edita(ProdutoDTO atualizaProduto, ModelMapper modelMapper, CategoriaService categoriaService) {
		this.nome = atualizaProduto.getNome();
		this.descricao = atualizaProduto.getDescricao();
		this.referencia = atualizaProduto.getReferencia();
		this.valorUnitario = atualizaProduto.getValorUnitario();
		if (atualizaProduto.getCategoria() != null && atualizaProduto.getCategoria().getNome() != null) {
	        Categoria categoria = categoriaService.obterOuCriarCategoria(atualizaProduto.getCategoria().getNome());
	        this.categoria = categoria;
	    }
		
	}
}
