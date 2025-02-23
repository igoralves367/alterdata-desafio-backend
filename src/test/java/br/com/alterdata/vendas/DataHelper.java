package br.com.alterdata.vendas;

import java.math.BigDecimal;
import java.util.List;

import org.mockito.Mockito;

import br.com.alterdata.vendas.dto.CategoriaEdicaoRequest;
import br.com.alterdata.vendas.dto.CategoriaRequest;
import br.com.alterdata.vendas.dto.CategoriaResponse;
import br.com.alterdata.vendas.dto.LoginRequest;
import br.com.alterdata.vendas.dto.ProdutoCriacaoRequest;
import br.com.alterdata.vendas.dto.ProdutoEdicaoRequest;
import br.com.alterdata.vendas.dto.ProdutoListaResponse;
import br.com.alterdata.vendas.dto.ProdutoResponse;
import br.com.alterdata.vendas.dto.UserRequest;
import br.com.alterdata.vendas.model.Categoria;
import br.com.alterdata.vendas.model.Produto;
import br.com.alterdata.vendas.model.User;
import br.com.alterdata.vendas.service.CategoriaService;

public class DataHelper {

    public static Categoria createCategoria() {
        return new Categoria(createCategoriaRequest());
    }

    public static CategoriaRequest createCategoriaRequest() {
        return CategoriaRequest.builder()
                .id(1L)
                .nome("Eletrônicos")
                .build();
    }

    public static CategoriaEdicaoRequest createCategoriaEdicaoRequest() {
        return CategoriaEdicaoRequest.builder()
                .nome("Casa & Decoração")
                .build();
    }

    public static CategoriaResponse createCategoriaResponse() {
        return new CategoriaResponse(createCategoria());
    }

    public static List<Categoria> createCategoriaList() {
        return List.of(
            createCategoria(),
            createCategoria(),
            createCategoria()
        );
    }

    public static List<CategoriaResponse> createCategoriaResponseList() {
        return CategoriaResponse.converte(createCategoriaList());
    }

    public static Produto createProduto() {
        CategoriaService categoriaServiceMock = Mockito.mock(CategoriaService.class);
        Mockito.when(categoriaServiceMock.buscaCategoriaPorId(1L)).thenReturn(createCategoria());

        return new Produto(createProdutoRequest(), categoriaServiceMock);
    }

    public static ProdutoCriacaoRequest createProdutoRequest() {
        return ProdutoCriacaoRequest.builder()
                .nome("Smartphone")
                .descricao("Smartphone de última geração")
                .referencia("REF1234")
                .valorUnitario(new BigDecimal("2999.99"))
                .idCategoria(1L)
                .build();
    }

    public static ProdutoEdicaoRequest createProdutoEdicaoRequest() {
        return ProdutoEdicaoRequest.builder()
                .nome("Smartphone Pro")
                .descricao("Edição avançada do Smartphone")
                .referencia("REF5678")
                .valorUnitario(new BigDecimal("3999.99"))
                .idCategoria(1L)
                .build();
    }

    public static ProdutoResponse createProdutoResponse() {
        return new ProdutoResponse(createProduto());
    }

    public static ProdutoListaResponse createProdutoListaResponse() {
        return new ProdutoListaResponse(createProduto());
    }

    public static List<Produto> createProdutoList() {
        return List.of(
            createProduto(),
            createProduto(),
            createProduto()
        );
    }

    public static List<ProdutoListaResponse> createProdutoListaResponseList() {
        return ProdutoListaResponse.converte(createProdutoList());
    }
    
    
    public static User createUser() {
        return User.builder()
                .id(1)
                .login("admin")
                .password("senha123")
                .admin(true)
                .build();
    }

    public static UserRequest createUserDTO() {
        UserRequest userDTO = new UserRequest();
        userDTO.setLogin("admin");
        userDTO.setPassword("senha123");
        userDTO.setAdmin(true);
        return userDTO;
    }

    public static LoginRequest createLoginDTO() {
        LoginRequest loginDTO = new LoginRequest();
        loginDTO.setLogin("admin");
        loginDTO.setPassword("senha123");
        return loginDTO;
    }
}
