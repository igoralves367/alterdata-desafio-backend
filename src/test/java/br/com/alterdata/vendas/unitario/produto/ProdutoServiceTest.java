package br.com.alterdata.vendas.unitario.produto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import br.com.alterdata.vendas.DataHelper;
import br.com.alterdata.vendas.dto.CategoriaDTO;
import br.com.alterdata.vendas.dto.ProdutoDTO;
import br.com.alterdata.vendas.handler.APIException;
import br.com.alterdata.vendas.model.Categoria;
import br.com.alterdata.vendas.model.Produto;
import br.com.alterdata.vendas.repository.ProdutoRepository;
import br.com.alterdata.vendas.service.CategoriaService;
import br.com.alterdata.vendas.service.ProdutoService;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {

	@Mock 
	private ProdutoRepository produtoRepository;
    
	@Mock 
    private CategoriaService categoriaService;
   
	@Mock 
    private ModelMapper modelMapper;

    @InjectMocks 
    private ProdutoService produtoService; 

    private Produto produto;
    private ProdutoDTO produtoDTO;

    @BeforeEach
    void setup() {
        produto = DataHelper.createProduto();
        produtoDTO = DataHelper.createProdutoDTO();
    }

    @Test
    @DisplayName("Deveria listar todos os produtos")
    void deveriaListarTodosProdutos() {
        when(produtoRepository.findAll()).thenReturn(List.of(produto)); 
        when(modelMapper.map(produto, ProdutoDTO.class)).thenReturn(produtoDTO); 

        List<ProdutoDTO> produtos = produtoService.listar(); 

        assertNotNull(produtos); 
        assertEquals(1, produtos.size()); 
        assertEquals("Smartphone", produtos.get(0).getNome()); 

        verify(produtoRepository, times(1)).findAll(); 
        verify(modelMapper, times(1)).map(produto, ProdutoDTO.class); 
    }

    @Test
    @DisplayName("Deveria criar um produto")
    void deveriaCriarProduto() {
        when(produtoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(false);
        when(categoriaService.obterOuCriarCategoria(anyString())).thenReturn(produto.getCategoria());
        when(modelMapper.map(produtoDTO, Produto.class)).thenReturn(produto);
        when(produtoRepository.save(produto)).thenReturn(produto);
        when(modelMapper.map(produto, ProdutoDTO.class)).thenReturn(produtoDTO);

        ProdutoDTO produtoCriado = produtoService.criaProduto(produtoDTO);

        assertNotNull(produtoCriado);
        assertEquals("Smartphone", produtoCriado.getNome());
    }

    @Test
    @DisplayName("Deveria lançar erro ao criar um produto com nome já existente")
    void deveriaLancarErroProdutoExistente() {
        when(produtoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(true);

        APIException exception = assertThrows(APIException.class, () -> produtoService.criaProduto(produtoDTO));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusException());
        assertEquals("Já existe um produto com esse nome.", exception.getMessage());
    }

    @Test
    @DisplayName("Deveria buscar um produto por ID")
    void deveriaBuscarProdutoPorId() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(modelMapper.map(produto, ProdutoDTO.class)).thenReturn(produtoDTO);

        ProdutoDTO produtoEncontrado = produtoService.buscarProdutoPorId(1L);

        assertNotNull(produtoEncontrado);
        assertEquals("Smartphone", produtoEncontrado.getNome());
    }
    
    @Test
    @DisplayName("Deveria lançar erro ao buscar produto inexistente por ID")
    void deveriaLancarErroProdutoNaoEncontrado() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        APIException exception = assertThrows(APIException.class, () -> produtoService.buscarProdutoPorId(1L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusException());
        assertEquals("Produto não encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("Deveria alterar um produto existente")
    void deveriaAlterarProduto() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(modelMapper.map(produto, ProdutoDTO.class)).thenReturn(produtoDTO);
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        ProdutoDTO produtoAlterado = produtoService.alterarProduto(1L, produtoDTO);

        assertNotNull(produtoAlterado);
        assertEquals("Smartphone", produtoAlterado.getNome());
    }

    @Test
    @DisplayName("Deveria lançar erro ao tentar alterar um produto inexistente")
    void deveriaLancarErroAoAlterarProdutoInexistente() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        APIException exception = assertThrows(APIException.class, () -> produtoService.alterarProduto(1L, produtoDTO));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusException());
        assertEquals("Produto não encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("Deveria deletar um produto existente")
    void deveriaDeletarProduto() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        doNothing().when(produtoRepository).delete(produto);

        assertDoesNotThrow(() -> produtoService.deletarProduto(1L));

        verify(produtoRepository, times(1)).delete(produto);
    }

    @Test
    @DisplayName("Deveria lançar erro ao tentar deletar um produto inexistente")
    void deveriaLancarErroAoDeletarProdutoInexistente() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        APIException exception = assertThrows(APIException.class, () -> produtoService.deletarProduto(1L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusException());
        assertEquals("Produto não encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("Deveria buscar produtos por categoria")
    void deveriaBuscarProdutosPorCategoria() {
        CategoriaDTO categoriaDTO = DataHelper.createCategoriaDTO();
        Categoria categoria = DataHelper.createCategoria();

        when(categoriaService.obterCategoriaPorNome(anyString())).thenReturn(categoriaDTO);
        
        when(modelMapper.map(categoriaDTO, Categoria.class)).thenReturn(categoria);

        when(produtoRepository.findByCategoria(any())).thenReturn(List.of(produto));
        
        when(modelMapper.map(produto, ProdutoDTO.class)).thenReturn(produtoDTO);

        List<ProdutoDTO> produtos = produtoService.buscarProdutosPorCategoria("Eletrônicos");

        assertNotNull(produtos);
        assertEquals(1, produtos.size());
        assertEquals("Smartphone", produtos.get(0).getNome());

        verify(produtoRepository, times(1)).findByCategoria(any());
    }


    @Test
    @DisplayName("Deveria retornar lista vazia se categoria não for encontrada")
    void deveriaRetornarListaVaziaSeCategoriaNaoForEncontrada() {
        when(categoriaService.obterCategoriaPorNome(anyString())).thenReturn(null);

        List<ProdutoDTO> produtos = produtoService.buscarProdutosPorCategoria("Eletrônicos");

        assertNotNull(produtos);
        assertTrue(produtos.isEmpty());
    }

    @Test
    @DisplayName("Deveria pesquisar produtos por termo")
    void deveriaPesquisarProdutosPorTermo() {
        when(produtoRepository.buscarProdutosPorTermo(anyString())).thenReturn(List.of(produto));
        when(modelMapper.map(produto, ProdutoDTO.class)).thenReturn(produtoDTO);

        List<ProdutoDTO> produtos = produtoService.pesquisarProdutos("Smartphone");

        assertNotNull(produtos);
        assertEquals(1, produtos.size());
        assertEquals("Smartphone", produtos.get(0).getNome());
    }

    @Test
    @DisplayName("Deveria lançar erro ao pesquisar produtos e não encontrar resultados")
    void deveriaLancarErroAoPesquisarProdutosSemResultados() {
        when(produtoRepository.buscarProdutosPorTermo(anyString())).thenReturn(Collections.emptyList());

        APIException exception = assertThrows(APIException.class, () -> produtoService.pesquisarProdutos("Inexistente"));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusException());
        assertEquals("Nenhum produto encontrado para o termo: Inexistente", exception.getMessage());
    }
}
