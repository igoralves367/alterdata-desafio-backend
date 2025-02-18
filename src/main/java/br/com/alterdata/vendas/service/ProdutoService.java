package br.com.alterdata.vendas.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alterdata.vendas.dto.ProdutoDTO;
import br.com.alterdata.vendas.model.Produto;
import br.com.alterdata.vendas.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProdutoService {

    @Autowired private ProdutoRepository produtoRepository;
    
    private final ModelMapper modelMapper;

    public List<Produto> listar() {
        return produtoRepository.findAll();
    }

    public ProdutoDTO criaProduto(ProdutoDTO produtoDTO) {
    	log.info("[start] ProdutoService - criaProduto");
        Produto produto = modelMapper.map(produtoDTO, Produto.class);
        Produto produtoSalvo = produtoRepository.save(produto);
        log.info("[finish] ProdutoService - criaProduto");
        return modelMapper.map(produtoSalvo, ProdutoDTO.class);
        
    }
}
