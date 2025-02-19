package br.com.alterdata.vendas.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alterdata.vendas.dto.ProdutoDTO;
import br.com.alterdata.vendas.model.Produto;
import br.com.alterdata.vendas.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("produtos")
@RequiredArgsConstructor
@Log4j2
public class ProdutosController {

    private final ProdutoService produtoService;
    
    @GetMapping("/listarProdutos")
    public List<Produto> listar() {
        return produtoService.listar();
    }

	@PostMapping
	public ResponseEntity<ProdutoDTO> criaProduto(@RequestBody ProdutoDTO produto) {
		log.info("[start] ProdutosController - criaProduto");
		ProdutoDTO criaProduto = produtoService.criaProduto(produto);
		log.info("[finish] ProdutosController - criaProduto");
		return new ResponseEntity<>(criaProduto, HttpStatus.CREATED);
	}

	@GetMapping("/buscarProduto/{id}")
	public ResponseEntity<ProdutoDTO> buscarProdutoPorId(@PathVariable Long id) {
		log.info("[start] ProdutosController - buscarProdutoPorId");
		log.info("[idProduto] {}", id);
		ProdutoDTO produto = produtoService.buscarProdutoPorId(id);
		log.info("[finish] ProdutosController - buscarProdutoPorId");
		return new ResponseEntity<>(produto, HttpStatus.OK);

	}

    @PutMapping("/atualizaProduto/{id}")
    public ResponseEntity<ProdutoDTO> atualizarProduto(@PathVariable Long id, @RequestBody ProdutoDTO atualizaProduto) {
    	log.info("[start] ProdutosController - atualizarProduto");
		log.info("[idProduto] {}", id);
        ProdutoDTO alterarProduto = produtoService.alterarProduto(id, atualizaProduto);
        log.info("[finish] ProdutosController - atualizarProduto");
        return new ResponseEntity<>(alterarProduto, HttpStatus.OK);
    }
    
    @DeleteMapping("/deletarProduto/{id}")
    public ResponseEntity<Void> excluirProduto(@PathVariable Long id) {
    	log.info("[start] ProdutosController - excluirProduto");
		log.info("[idProduto] {}", id);
        produtoService.deletarProduto(id);
        log.info("[finish] ProdutosController - excluirProduto");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @GetMapping("/produtos/categoria/{NomeCategoria}")
    public ResponseEntity<List<ProdutoDTO>> encontrarProdutosPorCategoria(@PathVariable String NomeCategoria) {
    	log.info("[start] ProdutosController - encontrarProdutosPorCategoria");
		log.info("[NomeCategoria] {}", NomeCategoria);
        List<ProdutoDTO> produtos = produtoService.buscarProdutosPorCategoria(NomeCategoria);
        log.info("[finish] ProdutosController - encontrarProdutosPorCategoria");
        return new ResponseEntity<>(produtos, HttpStatus.OK);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pesquisar/{termo}")
    public ResponseEntity<List<ProdutoDTO>> pesquisarProdutos(@PathVariable String termo) {
        log.info("[start] ProdutosController - pesquisarProdutos");
        log.info("[termo] {}", termo);
        List<ProdutoDTO> produtos = produtoService.pesquisarProdutos(termo);
        log.info("[finish] ProdutosController - pesquisarProdutos");
        return new ResponseEntity<>(produtos, HttpStatus.OK);
    }
}
