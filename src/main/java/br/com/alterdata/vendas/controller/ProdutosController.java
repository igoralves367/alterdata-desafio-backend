package br.com.alterdata.vendas.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.alterdata.vendas.dto.ProdutoCriacaoRequest;
import br.com.alterdata.vendas.dto.ProdutoEdicaoRequest;
import br.com.alterdata.vendas.dto.ProdutoListaResponse;
import br.com.alterdata.vendas.dto.ProdutoResponse;
import br.com.alterdata.vendas.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("produtos")
@RequiredArgsConstructor
@Log4j2
public class ProdutosController {

	private final ProdutoService produtoService;

	@GetMapping
	public List<ProdutoListaResponse> listar() {
		log.debug("[start] ProdutosController - listar");
		List<ProdutoListaResponse> produtos = produtoService.listar();
		log.debug("[finish] ProdutosController - listar");
		return produtos;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProdutoResponse criaProduto(@RequestBody ProdutoCriacaoRequest produto) {
		log.debug("[start] ProdutosController - criaProduto");
		ProdutoResponse criaProduto = produtoService.criaProduto(produto);
		log.debug("[finish] ProdutosController - criaProduto");
		return criaProduto;
	}

	@GetMapping("/{id}")
	public ProdutoListaResponse buscarProdutoPorId(@PathVariable Long id) {
		log.debug("[start] ProdutosController - buscarProdutoPorId");
		log.debug("[idProduto] {}", id);
		ProdutoListaResponse buscaProduto = produtoService.buscarProdutoPorId(id);
		log.debug("[finish] ProdutosController - buscarProdutoPorId");
		return buscaProduto;

	}

	@PutMapping("/{id}")
	public ProdutoResponse atualizarProduto(@PathVariable Long id, @RequestBody ProdutoEdicaoRequest atualizaProduto) {
		log.debug("[start] ProdutosController - atualizarProduto");
		log.debug("[idProduto] {}", id);
		ProdutoResponse alterarProduto = produtoService.alterarProduto(id, atualizaProduto);
		log.debug("[finish] ProdutosController - atualizarProduto");
		return alterarProduto;
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluirProduto(@PathVariable Long id) {
		log.debug("[start] ProdutosController - excluirProduto");
		log.debug("[idProduto] {}", id);
		produtoService.deletarProduto(id);
		log.debug("[finish] ProdutosController - excluirProduto");
	}

	@GetMapping("/categoria")
	public List<ProdutoListaResponse> encontrarProdutosPorCategoria(@RequestParam(required = false) String categoria) {
		log.debug("[start] ProdutosController - encontrarProdutosPorCategoria");
		log.debug("[categoria] {}", categoria);
		List<ProdutoListaResponse> produtos = produtoService.buscarProdutosPorCategoria(categoria);
		log.debug("[finish] ProdutosController - encontrarProdutosPorCategoria");
		return produtos;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/pesquisa")
	public List<ProdutoListaResponse> pesquisarProdutos(@RequestParam String termo) {
		log.debug("[start] ProdutosController - pesquisarProdutos");
		log.debug("[termo] {}", termo);
		List<ProdutoListaResponse> produtos = produtoService.pesquisarProdutos(termo);
		log.debug("[finish] ProdutosController - pesquisarProdutos");
		return produtos;
	}
}
