package br.com.alterdata.vendas.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.alterdata.vendas.dto.CategoriaDTO;
import br.com.alterdata.vendas.handler.APIException;
import br.com.alterdata.vendas.model.Categoria;
import br.com.alterdata.vendas.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class CategoriaService {

	public final CategoriaRepository categoriaRepository;
	private final ModelMapper modelMapper;

	public CategoriaDTO salvarCategoria(CategoriaDTO categoriaDto) {
		log.info("[start] CategoriaService - salvarCategoria");
		if (categoriaRepository.existsByNomeIgnoreCase(categoriaDto.getNome())) {
			throw APIException.build(HttpStatus.BAD_REQUEST, "Categoria já cadastrada.");
		}

		Categoria categoria = modelMapper.map(categoriaDto, Categoria.class);
		Categoria categoriaSalva = categoriaRepository.save(categoria);
		log.info("[finish] CategoriaService - salvarCategoria");
		return modelMapper.map(categoriaSalva, CategoriaDTO.class);
	}

	public Categoria obterOuCriarCategoria(String nomeCategoria) {
		log.info("[start] CategoriaService - obterOuCriarCategoria");
		Categoria categoria = categoriaRepository.findByNome(nomeCategoria);
		if (categoria == null) {
			categoria = new Categoria();
			categoria.setNome(nomeCategoria);
			categoria = categoriaRepository.save(categoria);
		}
		log.info("[finish] CategoriaService - obterOuCriarCategoria");
		return categoria;
	}

	public CategoriaDTO obterCategoriaPorNome(String nomeCategoria) {
		log.info("[start] CategoriaService - obterCategoriaPorNome");
		log.info("[nomeCategoria] {}", nomeCategoria);
		Categoria categoria = categoriaRepository.findByNome(nomeCategoria);
		if (categoria == null) {
			throw APIException.build(HttpStatus.NOT_FOUND, "Categoria não encontrada");
		}
		log.info("[finish] CategoriaService - obterCategoriaPorNome");
		return modelMapper.map(categoria, CategoriaDTO.class);
	}

	public List<CategoriaDTO> listaCategorias() {
		log.info("[start] CategoriaService - listaCategorias");
		List<Categoria> categorias = categoriaRepository.findAll();
		log.info("[finish] CategoriaService - listaCategorias");
		return categorias.stream()
				.map(categoria -> modelMapper.map(categoria, CategoriaDTO.class))
				.collect(Collectors.toList());
	}

	public CategoriaDTO buscarCategoriaPorId(Long id) {
		log.info("[start] CategoriaService - buscarCategoriaPorId");
		log.info("[idCategoria] {}", id);
		 Categoria categoria = categoriaRepository.findById(id)
				 .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
		 log.info("[finish] CategoriaService - buscarCategoriaPorId");
	        return modelMapper.map(categoria, CategoriaDTO.class);
	    }

	public void excluirCategoria(Long id) {
		log.info("[start] ProdutoService - excluirCategoria");
		log.info("[idCategoria] {}", id);
		Categoria categoria = categoriaRepository.findById(id)
				 .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
		categoriaRepository.delete(categoria);
		log.info("[finish] ProdutoService - excluirCategoria");
		
	}

	public CategoriaDTO atualizarCategoria(Long id, String novoNomeCategoria) {
		log.info("[start] CategoriaService - atualizarCategoria");
		log.info("[idCategoria] {}", id);
		Categoria categoriaAtual = categoriaRepository.findById(id)
				.orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
        categoriaAtual.setNome(novoNomeCategoria);
        log.info("[finish] CategoriaService - atualizarCategoria");
        Categoria updatedCategoria = categoriaRepository.save(categoriaAtual);
        return modelMapper.map(updatedCategoria, CategoriaDTO.class);
	}
}
