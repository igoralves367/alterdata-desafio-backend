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

	@Query("SELECT p FROM Produto p " + "WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :termo, '%')) "
			+ "OR LOWER(p.descricao) LIKE LOWER(CONCAT('%', :termo, '%')) "
			+ "OR LOWER(p.referencia) LIKE LOWER(CONCAT('%', :termo, '%')) "
			+ "OR LOWER(p.categoria.nome) LIKE LOWER(CONCAT('%', :termo, '%')) "
			+ "OR (CAST(:termo AS string) IS NOT NULL AND CAST(p.valorUnitario AS string) LIKE CONCAT('%', :termo, '%'))")
	List<Produto> buscarProdutosPorTermo(@Param("termo") String termo);

}
