package br.com.alterdata.vendas.repository;

import br.com.alterdata.vendas.model.Categoria;
import br.com.alterdata.vendas.model.Produto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

	List<Produto> findByCategoria(Categoria map);

	boolean existsByNomeIgnoreCase(String nome);
	
}
