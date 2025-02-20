package br.com.alterdata.vendas;

import java.math.BigDecimal;

import br.com.alterdata.vendas.dto.CategoriaDTO;
import br.com.alterdata.vendas.dto.LoginDTO;
import br.com.alterdata.vendas.dto.ProdutoDTO;
import br.com.alterdata.vendas.dto.UserDTO;
import br.com.alterdata.vendas.model.Categoria;
import br.com.alterdata.vendas.model.Produto;
import br.com.alterdata.vendas.model.User;

public class DataHelper {

	public static Categoria createCategoria() {
		return new Categoria(1L, "Eletrônicos");
	}

	public static CategoriaDTO createCategoriaDTO() {
		return new CategoriaDTO(1L, "Eletrônicos");
	}

	public static Produto createProduto() {
		return new Produto(1L, "Smartphone", "Smartphone de última geração", "REF1234", new BigDecimal("2999.99"),
				createCategoria());
	}

	public static ProdutoDTO createProdutoDTO() {
		return new ProdutoDTO(2L, "Smartphone", "Smartphone de última geração", "REF1234", new BigDecimal("2999.99"),
				createCategoriaDTO());
	}

	public static User createUser() {
		return new User(1, "admin", "senha123", true);
	}

	public static UserDTO createUserDTO() {
		UserDTO userDTO = new UserDTO();
		userDTO.setLogin("admin");
		userDTO.setPassword("senha123");
		userDTO.setAdmin(true);
		return userDTO;
	}

	public static LoginDTO createLoginDTO() {
		LoginDTO loginDTO = new LoginDTO();
		loginDTO.setLogin("admin");
		loginDTO.setPassword("senha123");
		return loginDTO;
	}

//	public static User createUser() {
//		return new User((int) 1L, "admin", "senha123", true);
//	}
//
//	public static UserDTO createUserDTO() {
//		return new UserDTO();
//	}
//
//	public static LoginDTO createLoginDTO() {
//		return new LoginDTO();
//	}

}
