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
        CategoriaRequest categoriaRequest = new CategoriaRequest();
        categoriaRequest.setNome("Eletrônicos");
        return categoriaRequest;
    }

    public static CategoriaEdicaoRequest createCategoriaEdicaoRequest() {
        CategoriaEdicaoRequest categoriaEdicaoRequest = new CategoriaEdicaoRequest();
        categoriaEdicaoRequest.setNome("Casa & Decoração");
        return categoriaEdicaoRequest;
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
        ProdutoCriacaoRequest produtoRequest = new ProdutoCriacaoRequest();
        produtoRequest.setNome("Smartphone");
        produtoRequest.setDescricao("Smartphone de última geração");
        produtoRequest.setReferencia("REF1234");
        produtoRequest.setValorUnitario(new BigDecimal("2999.99"));
        produtoRequest.setIdCategoria(1L);
        return produtoRequest;
    }

    public static ProdutoEdicaoRequest createProdutoEdicaoRequest() {
        ProdutoEdicaoRequest produtoEdicaoRequest = new ProdutoEdicaoRequest();
        produtoEdicaoRequest.setNome("Smartphone Pro");
        produtoEdicaoRequest.setDescricao("Edição avançada do Smartphone");
        produtoEdicaoRequest.setReferencia("REF5678");
        produtoEdicaoRequest.setValorUnitario(new BigDecimal("3999.99"));
        produtoEdicaoRequest.setIdCategoria(1L);
        return produtoEdicaoRequest;
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
