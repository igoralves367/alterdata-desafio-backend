package br.com.alterdata.vendas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.alterdata.vendas.model.Categoria;
import br.com.alterdata.vendas.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

	List<Produto> findByCategoria(Categoria map);

	boolean existsByNomeIgnoreCase(String nome);

	@Query("SELECT p FROM Produto p LEFT JOIN FETCH p.categoria c " +
		       "WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :termo, '%')) " +
		       "OR LOWER(p.descricao) LIKE LOWER(CONCAT('%', :termo, '%')) " +
		       "OR LOWER(p.referencia) LIKE LOWER(CONCAT('%', :termo, '%')) " +
		       "OR LOWER(c.nome) LIKE LOWER(CONCAT('%', :termo, '%'))")
		List<Produto> buscarProdutosPorTermo(@Param("termo") String termo);

}
