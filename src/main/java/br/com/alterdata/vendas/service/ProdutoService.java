package br.com.alterdata.vendas.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.alterdata.vendas.dto.ProdutoCriacaoRequest;
import br.com.alterdata.vendas.dto.ProdutoEdicaoRequest;
import br.com.alterdata.vendas.dto.ProdutoListaResponse;
import br.com.alterdata.vendas.dto.ProdutoResponse;
import br.com.alterdata.vendas.handler.APIException;
import br.com.alterdata.vendas.model.Produto;
import br.com.alterdata.vendas.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProdutoService {

	private final ProdutoRepository produtoRepository;
	private final CategoriaService categoriaService;

	public List<ProdutoListaResponse> listar() {
		log.debug("[start] ProdutoService - listar");
		List<Produto> produtos = produtoRepository.findAllWithCategoria();
		log.debug("[finish] ProdutoService - listar");
		return ProdutoListaResponse.converte(produtos);
	}
	
	public ProdutoResponse criaProduto(ProdutoCriacaoRequest produtoDTO) {
		log.debug("[start] ProdutoService - criaProduto");
		Produto produto = new Produto(produtoDTO, categoriaService);
		produtoRepository.save(produto);
		log.debug("[finish] ProdutoService - criaProduto");
		return new ProdutoResponse(produto);
	}

	public ProdutoListaResponse buscarProdutoPorId(Long id) {
		log.debug("[start] ProdutoService - buscarProdutoPorId");
		log.debug("[idProduto] {}", id);
		Produto produto = buscaProdutoPorId(id);
		log.debug("[finish] ProdutoService - buscarProdutoPorId");
		return new ProdutoListaResponse(produto);
	}

	public ProdutoResponse alterarProduto(Long id, ProdutoEdicaoRequest atualizaProduto) {
		log.debug("[start] ProdutoService - alterarProduto");
		log.debug("[idProduto] {}", id);
		Produto produtoNovo = buscaProdutoPorId(id);
		produtoNovo.edita(atualizaProduto, categoriaService);
		produtoRepository.save(produtoNovo);
		log.debug("[finish] ProdutoService - alterarProduto");
		return new ProdutoResponse(produtoNovo);
	}

	public void deletarProduto(Long id) {
		log.debug("[start] ProdutoService - deletarProduto");
		log.debug("[idProduto] {}", id);
		Produto produto = buscaProdutoPorId(id);
		produtoRepository.delete(produto);
		log.debug("[finish] ProdutoService - deletarProduto");
	}

	private Produto buscaProdutoPorId(Long id) {
		return produtoRepository.findByIdWithCategoria(id)
				.orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Produto não encontrado"));
	}
	
	public List<ProdutoListaResponse> buscarProdutosPorCategoria(String NomeCategoria) {
		log.debug("[start] ProdutoService - buscarProdutosPorCategoria");
		log.debug("[Categoria] {}", NomeCategoria);
		List<Produto> produtos = produtoRepository.findByCategoria(NomeCategoria);
		log.debug("[finish] ProdutoService - buscarProdutosPorCategoria");
		return ProdutoListaResponse.converte(produtos);
	}
	
	public List<ProdutoListaResponse> pesquisarProdutos(String termo) {
		log.debug("[start] ProdutoService - pesquisarProdutos");
		log.debug("[termo] {}", termo);
		List<Produto> produtos = produtoRepository.buscarProdutosPorTermo(termo);
		log.debug("[finish] ProdutoService - pesquisarProdutos");
		return ProdutoListaResponse.converte(produtos);
	}
}
