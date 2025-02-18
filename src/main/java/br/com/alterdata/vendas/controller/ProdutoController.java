package br.com.alterdata.vendas.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alterdata.vendas.dto.ProdutoDTO;
import br.com.alterdata.vendas.service.ProdutoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("produtos")
@RequiredArgsConstructor
public class ProdutoController {
	
	private final ProdutoService produtoService;

	@PostMapping
	public ResponseEntity<ProdutoDTO> criaProduto(@RequestBody ProdutoDTO produto) {
		ProdutoDTO criaProduto = produtoService.criaProduto(produto);
		return new ResponseEntity<>(criaProduto, HttpStatus.CREATED);
	}

}
