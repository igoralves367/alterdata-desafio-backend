package br.com.alterdata.vendas.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
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

import br.com.alterdata.vendas.dto.CategoriaEdicaoRequest;
import br.com.alterdata.vendas.dto.CategoriaRequest;
import br.com.alterdata.vendas.dto.CategoriaResponse;
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
	@ResponseStatus(HttpStatus.CREATED)
	public CategoriaResponse criarCategoria(@RequestBody CategoriaRequest categoria) {
		log.debug("[start] CategoriaController - criarCategoria");
		CategoriaResponse criarCategoria = categoriaService.salvarCategoria(categoria);
		log.debug("[finish] CategoriaController - criarCategoria");
		return criarCategoria;
	}

	@GetMapping
	public List<CategoriaResponse> listar() {
		log.debug("[start] CategoriaController - ListaCategorias");
		List<CategoriaResponse> categorias = categoriaService.listaCategorias();
		log.debug("[finish] CategoriaController - ListaCategorias");
		return categorias;
	}

	@GetMapping("/{id}")
	public CategoriaResponse buscarCategoriaPorId(@PathVariable Long id) {
		log.debug("[start] CategoriaController - buscarCategoriaPorId");
		log.debug("[idCategoria] {}", id);
		CategoriaResponse categoria = categoriaService.buscarCategoriaPorId(id);
		log.debug("[finish] CategoriaController - buscarCategoriaPorId");
		return categoria;
	}
	
	@GetMapping("/pesquisa")
	public CategoriaResponse encontrarCategoriaPorNome(@RequestParam String nome) {
		log.debug("[start] CategoriaController - encontrarCategoriaPorNome");
		log.debug("[nomeCategoria] {}", nome);
		CategoriaResponse categoria = categoriaService.obterCategoriaPorNome(nome);
		log.debug("[finish] CategoriaController - encontrarCategoriaPorNome");
		return categoria;
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluirCategoria(@PathVariable Long id) {
		log.debug("[start] CategoriaController - excluirCategoria");
		log.debug("[idCategoria] {}", id);
		categoriaService.excluirCategoria(id);
		log.debug("[finish] CategoriaController - excluirCategoria");
	}

	@PutMapping("/{id}")
	public CategoriaResponse atualizarCategoria(@PathVariable Long id,
			@RequestBody CategoriaEdicaoRequest atualizaCategoria) {
		log.debug("[start] CategoriaController - atualizarCategoria");
		log.debug("[idCategoria] {}", id);
		CategoriaResponse alterarCategoria = categoriaService.atualizarCategoria(id, atualizaCategoria);
		log.debug("[start] CategoriaController - atualizarCategoria");
		return alterarCategoria;
	}
	
}
