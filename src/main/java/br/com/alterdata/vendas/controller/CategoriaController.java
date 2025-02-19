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

import br.com.alterdata.vendas.dto.CategoriaDTO;
import br.com.alterdata.vendas.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("categorias")
@RequiredArgsConstructor
@Log4j2
public class CategoriaController {

	private final CategoriaService categoriaService;

	@PostMapping
	public ResponseEntity<CategoriaDTO> criarCategoria(@RequestBody CategoriaDTO categoria) {
		log.info("[start] CategoriaController - criarCategoria");
		CategoriaDTO criarCategoria = categoriaService.salvarCategoria(categoria);
		log.info("[finish] CategoriaController - criarCategoria");
		return new ResponseEntity<>(criarCategoria, HttpStatus.CREATED);
	}

	@GetMapping("/listarCategorias")
	public ResponseEntity<List<CategoriaDTO>> listar() {
		log.info("[start] CategoriaController - ListaCategorias");
		List<CategoriaDTO> ListaCategorias = categoriaService.listaCategorias();
		log.info("[finish] CategoriaController - ListaCategorias");
		return new ResponseEntity<>(ListaCategorias, HttpStatus.OK);
	}

	@GetMapping("/buscarProduto/{id}")
	public ResponseEntity<CategoriaDTO> buscarCategoriaPorId(@PathVariable Long id) {
		log.info("[start] CategoriaController - buscarCategoriaPorId");
		log.info("[idCategoria] {}", id);
		CategoriaDTO categoria = categoriaService.buscarCategoriaPorId(id);
		log.info("[finish] CategoriaController - buscarCategoriaPorId");
		return new ResponseEntity<>(categoria, HttpStatus.OK);
	}

	@GetMapping("/categoria/{nome}")
	public ResponseEntity<CategoriaDTO> encontrarCategoriaPorNome(@PathVariable String nome) {
		log.info("[start] CategoriaController - encontrarCategoriaPorNome");
		log.info("[nomeCategoria] {}", nome);
		CategoriaDTO categoria = categoriaService.obterCategoriaPorNome(nome);
		log.info("[finish] CategoriaController - encontrarCategoriaPorNome");
		return new ResponseEntity<>(categoria, HttpStatus.OK);
	}

	@DeleteMapping("/deletarCategoria/{id}")
	public ResponseEntity<Void> excluirCategoria(@PathVariable Long id) {
		log.info("[start] CategoriaController - excluirCategoria");
		log.info("[idCategoria] {}", id);
		categoriaService.excluirCategoria(id);
		log.info("[finish] CategoriaController - excluirCategoria");
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PutMapping("/ataulizaCategoria/{id}")
	public ResponseEntity<CategoriaDTO> atualizarCategoria(@PathVariable Long id,
			@RequestBody String novoNomeCategoria) {
		log.info("[start] CategoriaController - atualizarCategoria");
		log.info("[idCategoria] {}", id);
		CategoriaDTO updatedCategoria = categoriaService.atualizarCategoria(id, novoNomeCategoria);
		log.info("[start] CategoriaController - atualizarCategoria");
		return new ResponseEntity<>(updatedCategoria, HttpStatus.OK);
	}

}
