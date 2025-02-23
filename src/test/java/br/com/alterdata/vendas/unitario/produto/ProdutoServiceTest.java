package br.com.alterdata.vendas.unitario.produto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import br.com.alterdata.vendas.DataHelper;
import br.com.alterdata.vendas.dto.ProdutoCriacaoRequest;
import br.com.alterdata.vendas.dto.ProdutoEdicaoRequest;
import br.com.alterdata.vendas.dto.ProdutoListaResponse;
import br.com.alterdata.vendas.dto.ProdutoResponse;
import br.com.alterdata.vendas.handler.APIException;
import br.com.alterdata.vendas.model.Produto;
import br.com.alterdata.vendas.repository.ProdutoRepository;
import br.com.alterdata.vendas.service.CategoriaService;
import br.com.alterdata.vendas.service.ProdutoService;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @InjectMocks
    private ProdutoService produtoService;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private CategoriaService categoriaService;

    private Produto produto;
    private ProdutoCriacaoRequest produtoRequest;
    private ProdutoEdicaoRequest produtoEdicaoRequest;

    @BeforeEach
    void setup() {
        produto = DataHelper.createProduto();
        produtoRequest = DataHelper.createProdutoRequest();
        produtoEdicaoRequest = DataHelper.createProdutoEdicaoRequest();
    }

    @Test
    void deveriaListarTodosOsProdutos() {
        when(produtoRepository.findAllWithCategoria()).thenReturn(List.of(produto));

        List<ProdutoListaResponse> produtos = produtoService.listar();

        assertNotNull(produtos);
        assertEquals(1, produtos.size());
        assertEquals("Smartphone", produtos.get(0).getNome());

        verify(produtoRepository, times(1)).findAllWithCategoria();
    }

    @Test
    void deveriaCriarUmProduto() {
        when(categoriaService.buscaCategoriaPorId(produtoRequest.getIdCategoria())).thenReturn(produto.getCategoria());
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        ProdutoResponse produtoCriado = produtoService.criaProduto(produtoRequest);

        assertNotNull(produtoCriado);
        assertEquals("Smartphone", produtoCriado.getNome());

        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void deveriaBuscarProdutoPorId() {
        when(produtoRepository.findByIdWithCategoria(1L)).thenReturn(Optional.of(produto));

        ProdutoListaResponse produtoEncontrado = produtoService.buscarProdutoPorId(1L);

        assertNotNull(produtoEncontrado);
        assertEquals("Smartphone", produtoEncontrado.getNome());

        verify(produtoRepository, times(1)).findByIdWithCategoria(1L);
    }

    @Test
    void deveriaLancarErroAoBuscarProdutoInexistente() {
        when(produtoRepository.findByIdWithCategoria(99L)).thenReturn(Optional.empty());

        APIException exception = assertThrows(APIException.class, () -> produtoService.buscarProdutoPorId(99L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusException());
        assertEquals("Produto não encontrado", exception.getMessage());

        verify(produtoRepository, times(1)).findByIdWithCategoria(99L);
    }

    @Test
    void deveriaAlterarProduto() {
        when(produtoRepository.findByIdWithCategoria(1L)).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        ProdutoResponse produtoAlterado = produtoService.alterarProduto(1L, produtoEdicaoRequest);

        assertNotNull(produtoAlterado);
        assertEquals("Smartphone Pro", produtoAlterado.getNome());

        verify(produtoRepository, times(1)).save(produto);
    }

    @Test
    void deveriaLancarErroAoAlterarProdutoInexistente() {
        when(produtoRepository.findByIdWithCategoria(1L)).thenReturn(Optional.empty());

        APIException exception = assertThrows(APIException.class, () -> produtoService.alterarProduto(1L, produtoEdicaoRequest));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusException());
        assertEquals("Produto não encontrado", exception.getMessage());
    }

    @Test
    void deveriaDeletarProduto() {
        when(produtoRepository.findByIdWithCategoria(1L)).thenReturn(Optional.of(produto));
        doNothing().when(produtoRepository).delete(produto);

        assertDoesNotThrow(() -> produtoService.deletarProduto(1L));

        verify(produtoRepository, times(1)).delete(produto);
    }

    @Test
    void deveriaLancarErroAoDeletarProdutoInexistente() {
        when(produtoRepository.findByIdWithCategoria(99L)).thenReturn(Optional.empty());

        APIException exception = assertThrows(APIException.class, () -> produtoService.deletarProduto(99L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusException());
        assertEquals("Produto não encontrado", exception.getMessage());

        verify(produtoRepository, times(1)).findByIdWithCategoria(99L);
    }

    @Test
    void deveriaBuscarProdutosPorCategoria() {
        when(produtoRepository.findByCategoria("Eletrônicos")).thenReturn(List.of(produto));

        List<ProdutoListaResponse> produtos = produtoService.buscarProdutosPorCategoria("Eletrônicos");

        assertNotNull(produtos);
        assertEquals(1, produtos.size());
        assertEquals("Smartphone", produtos.get(0).getNome());

        verify(produtoRepository, times(1)).findByCategoria("Eletrônicos");
    }

    @Test
    void deveriaPesquisarProdutosPorTermo() {
        when(produtoRepository.buscarProdutosPorTermo("Smartphone")).thenReturn(List.of(produto));

        List<ProdutoListaResponse> produtos = produtoService.pesquisarProdutos("Smartphone");

        assertNotNull(produtos);
        assertEquals(1, produtos.size());
        assertEquals("Smartphone", produtos.get(0).getNome());

        verify(produtoRepository, times(1)).buscarProdutosPorTermo("Smartphone");
    }
}
