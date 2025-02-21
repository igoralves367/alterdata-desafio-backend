package br.com.alterdata.vendas.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.alterdata.vendas.dto.CategoriaDTO;
import br.com.alterdata.vendas.dto.ProdutoDTO;
import br.com.alterdata.vendas.handler.APIException;
import br.com.alterdata.vendas.model.Categoria;
import br.com.alterdata.vendas.model.Produto;
import br.com.alterdata.vendas.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProdutoService {

	private final ProdutoRepository produtoRepository;
	private final ModelMapper modelMapper;
	private final CategoriaService categoriaService;

	public List<ProdutoDTO> listar() {
		log.info("[start] ProdutoService - listar");
		List<Produto> produtos = produtoRepository.findAll();
		log.info("[finish] ProdutoService - listar");
		return produtos.stream()
                .map(produto -> modelMapper.map(produto, ProdutoDTO.class))
                .collect(Collectors.toList());
	}
	
	public ProdutoDTO criaProduto(ProdutoDTO produtoDTO) {
		log.info("[start] ProdutoService - criaProduto");
		if (produtoRepository.existsByNomeIgnoreCase(produtoDTO.getNome())) {
			throw APIException.build(HttpStatus.BAD_REQUEST, "Já existe um produto com esse nome.");
		}
		Categoria categoria = categoriaService.obterOuCriarCategoria(produtoDTO.getCategoria().getNome());
		Produto produto = modelMapper.map(produtoDTO, Produto.class);
		produto.setCategoria(categoria);
		Produto produtoSalvo = produtoRepository.save(produto);
		log.info("[finish] ProdutoService - criaProduto");

		return modelMapper.map(produtoSalvo, ProdutoDTO.class);
	}

	public ProdutoDTO buscarProdutoPorId(Long id) {
		log.info("[start] ProdutoService - buscarProdutoPorId");
		log.info("[idProduto] {}", id);
		Produto produto = produtoRepository.findById(id)
				.orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Produto não encontrado"));
		log.info("[finish] ProdutoService - buscarProdutoPorId");
		return modelMapper.map(produto, ProdutoDTO.class);
	}

	public ProdutoDTO alterarProduto(Long id, ProdutoDTO atualizaProduto) {
		log.info("[start] ProdutoService - alterarProduto");
		log.info("[idProduto] {}", id);
		Produto produtoAtual = produtoRepository.findById(id)
				.orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Produto não encontrado"));
		
		produtoAtual.edita(atualizaProduto, modelMapper, categoriaService);

		Produto updatedProduto = produtoRepository.save(produtoAtual);
		log.info("[finish] ProdutoService - alterarProduto");
		return modelMapper.map(updatedProduto, ProdutoDTO.class);
	}

	public void deletarProduto(Long id) {
		log.info("[start] ProdutoService - deletarProduto");
		log.info("[idProduto] {}", id);
		Produto produto = produtoRepository.findById(id)
				.orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Produto não encontrado"));
		produtoRepository.delete(produto);
		log.info("[finish] ProdutoService - deletarProduto");
	}

	public List<ProdutoDTO> buscarProdutosPorCategoria(String NomeCategoria) {
		log.info("[start] ProdutoService - buscarProdutosPorCategoria");
		log.info("[Categoria] {}", NomeCategoria);
		CategoriaDTO categoria = categoriaService.obterCategoriaPorNome(NomeCategoria);
		if (categoria == null) {
			return Collections.emptyList();
		}
		List<Produto> produtos = produtoRepository.findByCategoria(modelMapper.map(categoria, Categoria.class));
		log.info("[finish] ProdutoService - buscarProdutosPorCategoria");
		return produtos.stream().map(produto -> modelMapper.map(produto, ProdutoDTO.class))
				.collect(Collectors.toList());
	}
	
	public List<ProdutoDTO> pesquisarProdutos(String termo) {
		log.info("[start] ProdutoService - pesquisarProdutos");
		log.info("[termo] {}", termo);
		List<Produto> produtos = produtoRepository.buscarProdutosPorTermo(termo);
		
		 if (produtos.isEmpty()) {
			 throw APIException.build(HttpStatus.NOT_FOUND, "Nenhum produto encontrado para o termo: " + termo);
		    }
		log.info("[finish] ProdutoService - pesquisarProdutos");
		return produtos.stream().map(produto -> modelMapper.map(produto, ProdutoDTO.class))
				.collect(Collectors.toList());
	}
}
