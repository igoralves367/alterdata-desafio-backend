package br.com.alterdata.vendas.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private ProdutoRepository produtoRepository;

	private final ModelMapper modelMapper;
	private final CategoriaService categoriaService;

	public List<Produto> listar() {
		return produtoRepository.findAll();
	}

	public ProdutoDTO criaProduto(ProdutoDTO produtoDTO) {
		log.info("[start] ProdutoService - criaProduto");
		if (produtoDTO.getCategoria().getNome() != null) {
			Categoria categoria = categoriaService.obterOuCriarCategoria(produtoDTO.getCategoria().getNome());
			produtoDTO.setCategoria(modelMapper.map(categoria, CategoriaDTO.class));
		}
		Produto produto = modelMapper.map(produtoDTO, Produto.class);
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

	public ProdutoDTO alterarProdutoCategoria(Long id, String novaCategoria) {
		log.info("[start] ProdutoService - alterarProdutoCategoria");
		log.info("[idProduto] {}", id);
		Produto produto = produtoRepository.findById(id)
				.orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Produto não encontrado"));
		CategoriaDTO categoria = categoriaService.atualizarCategoria(produto.getCategoria().getId(), novaCategoria);
        produto.setCategoria(modelMapper.map(categoria, Categoria.class));

        Produto updatedProduto = produtoRepository.save(produto);
        log.info("[finish] ProdutoService - alterarProdutoCategoria");
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
}
