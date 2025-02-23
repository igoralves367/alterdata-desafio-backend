package br.com.alterdata.vendas.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class CategoriaRequest {
	private Long id;
    private String nome;
}
