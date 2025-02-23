package br.com.alterdata.vendas.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Setter
public class CategoriaRequest {
	private Long id;
    private String nome;
}
