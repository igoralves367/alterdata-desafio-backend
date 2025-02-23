package br.com.alterdata.vendas.dto;

import java.util.List;
import java.util.stream.Collectors;

import br.com.alterdata.vendas.model.Categoria;
import lombok.Getter;

@Getter
public class CategoriaResponse {

	private Long id;
    private String nome;
    
    public CategoriaResponse(Categoria categoria) {
    	this.id = categoria.getId();
		this.nome = categoria.getNome();
	}
    
    public static List<CategoriaResponse> converte(List<Categoria> categorias) {
        return categorias.stream()
                .map(CategoriaResponse::new) 
                .collect(Collectors.toList());
    }
}
