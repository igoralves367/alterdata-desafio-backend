package br.com.alterdata.vendas.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
        List<CategoriaDTO> ListaCategorias = categoriaService.listaCategorias();
        return new ResponseEntity<>(ListaCategorias, HttpStatus.OK);
    }
}
