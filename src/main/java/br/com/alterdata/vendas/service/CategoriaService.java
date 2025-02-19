package br.com.alterdata.vendas.service;

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
		Categoria categoria = categoriaRepository.findByNome(nomeCategoria);
		if (categoria == null) {
	        throw APIException.build(HttpStatus.NOT_FOUND, "Categoria não encontrada");
	    }
        log.info("[finish] CategoriaService - obterCategoriaPorNome");
        return modelMapper.map(categoria, CategoriaDTO.class);
	}

}
