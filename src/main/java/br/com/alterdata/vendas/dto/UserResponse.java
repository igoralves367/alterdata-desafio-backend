package br.com.alterdata.vendas.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;

@Getter
public class UserResponse {
	
	@NotEmpty
    private String login;

    @NotEmpty
    private String password;

    private boolean admin;

}
