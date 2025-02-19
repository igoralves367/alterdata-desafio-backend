package br.com.alterdata.vendas.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@RestController
@RequestMapping("produtos")
@RequiredArgsConstructor
public class ProdutosController {

    private final ProdutoService produtoService;
    
    @GetMapping("/listarProdutos")
    public List<Produto> listar() {
        return produtoService.listar();
    }

	@PostMapping
	public ResponseEntity<ProdutoDTO> criaProduto(@RequestBody ProdutoDTO produto) {
		ProdutoDTO criaProduto = produtoService.criaProduto(produto);
		return new ResponseEntity<>(criaProduto, HttpStatus.CREATED);
	}

	@GetMapping("/buscarProduto/{id}")
	public ResponseEntity<ProdutoDTO> buscarProdutoPorId(@PathVariable Long id) {
		ProdutoDTO produto = produtoService.buscarProdutoPorId(id);
		return new ResponseEntity<>(produto, HttpStatus.OK);

	}

    @PutMapping("/atualizaProduto/{id}")
    public ResponseEntity<ProdutoDTO> atualizarProduto(@PathVariable Long id, @RequestBody ProdutoDTO atualizaProduto) {
        ProdutoDTO alterarProduto = produtoService.alterarProduto(id, atualizaProduto);
        return new ResponseEntity<>(alterarProduto, HttpStatus.OK);
    }
    
    @DeleteMapping("/deletarProduto/{id}")
    public ResponseEntity<Void> excluirProduto(@PathVariable Long id) {
        produtoService.deletarProduto(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @GetMapping("/produtos/categoria/{NomeCategoria}")
    public ResponseEntity<List<ProdutoDTO>> encontrarProdutosPorCategoria(@PathVariable String NomeCategoria) {
        List<ProdutoDTO> produtos = produtoService.buscarProdutosPorCategoria(NomeCategoria);
        return new ResponseEntity<>(produtos, HttpStatus.OK);
    }
}
