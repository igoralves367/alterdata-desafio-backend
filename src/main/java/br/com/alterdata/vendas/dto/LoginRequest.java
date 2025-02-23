package br.com.alterdata.vendas.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequest {

	@NotEmpty
    private String login;

    @NotEmpty
    private String password;
}