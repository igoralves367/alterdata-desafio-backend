package br.com.alterdata.vendas.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.alterdata.vendas.dto.CategoriaEdicaoRequest;
import br.com.alterdata.vendas.dto.CategoriaRequest;
import br.com.alterdata.vendas.dto.CategoriaResponse;
import br.com.alterdata.vendas.handler.APIException;
import br.com.alterdata.vendas.model.Categoria;
import br.com.alterdata.vendas.repository.CategoriaRepository;
import br.com.alterdata.vendas.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class CategoriaService {

	public final CategoriaRepository categoriaRepository;
	public final ProdutoRepository produtoRepository;

	public CategoriaResponse salvarCategoria(CategoriaRequest categoriaRequest) {
		log.debug("[start] CategoriaService - salvarCategoria");
		Categoria categoria = new Categoria(categoriaRequest);
		categoriaRepository.save(categoria);
		log.debug("[finish] CategoriaService - salvarCategoria");
		return new CategoriaResponse(categoria);
	}

	public CategoriaResponse obterCategoriaPorNome(String nomeCategoria) {
		log.debug("[start] CategoriaService - obterCategoriaPorNome");
		log.debug("[nomeCategoria] {}", nomeCategoria);
		Categoria categoria = categoriaRepository.findByNome(nomeCategoria)
				.orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
		log.debug("[finish] CategoriaService - obterCategoriaPorNome");
		return new CategoriaResponse(categoria);
	}

	public List<CategoriaResponse> listaCategorias() {
		log.debug("[start] CategoriaService - listaCategorias");
		List<Categoria> categorias = categoriaRepository.findAll();
		log.debug("[finish] CategoriaService - listaCategorias");
		return CategoriaResponse.converte(categorias);
	}

	public CategoriaResponse buscarCategoriaPorId(Long id) {
		log.debug("[start] CategoriaService - buscarCategoriaPorId");
		log.debug("[idCategoria] {}", id);
		Categoria categoria = buscaPorId(id);
		log.debug("[finish] CategoriaService - buscarCategoriaPorId");
		return new CategoriaResponse(categoria);
	}

	private Categoria buscaPorId(Long id) {
		Categoria categoria = categoriaRepository.findById(id)
				.orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
		return categoria;
	}

	public Categoria buscaCategoriaPorId(Long id) {
		log.debug("[start] CategoriaService - buscarCategoriaPorId");
		log.debug("[idCategoria] {}", id);
		Categoria categoria = buscaPorId(id);
		log.debug("[finish] CategoriaService - buscarCategoriaPorId");
		return categoria;
	}
	
	public void excluirCategoria(Long id) {
	    log.debug("[start] CategoriaService - excluirCategoria");
	    log.debug("[idCategoria] {}", id);
	    Categoria categoria = buscaPorId(id);
	    boolean possuiProdutos = produtoRepository.existsByCategoria(categoria);
	    if (possuiProdutos) {
	        throw APIException.build(HttpStatus.BAD_REQUEST, "Não é possível excluir a categoria pois há produtos associados a ela.");
	    }
	    categoriaRepository.delete(categoria);
	    log.debug("[finish] CategoriaService - excluirCategoria");
	}

	public CategoriaResponse atualizarCategoria(Long id, CategoriaEdicaoRequest atualizaCategoria) {
		log.debug("[start] CategoriaService - atualizarCategoria");
		log.debug("[idCategoria] {}", id);
		Categoria categoriaNova = buscaPorId(id);
		categoriaNova.edita(atualizaCategoria);
		categoriaRepository.save(categoriaNova);
		log.debug("[finish] CategoriaService - atualizarCategoria");
		return new CategoriaResponse(categoriaNova);
	}
}
