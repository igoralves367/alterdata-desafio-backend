package br.com.alterdata.vendas.unitario.categoria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
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
import br.com.alterdata.vendas.dto.CategoriaEdicaoRequest;
import br.com.alterdata.vendas.dto.CategoriaRequest;
import br.com.alterdata.vendas.dto.CategoriaResponse;
import br.com.alterdata.vendas.handler.APIException;
import br.com.alterdata.vendas.model.Categoria;
import br.com.alterdata.vendas.repository.CategoriaRepository;
import br.com.alterdata.vendas.repository.ProdutoRepository;
import br.com.alterdata.vendas.service.CategoriaService;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {
	@InjectMocks
	private CategoriaService categoriaService;

	@Mock
	private CategoriaRepository categoriaRepository;

	@Mock
	private ProdutoRepository produtoRepository;

	private Categoria categoria;
	private CategoriaRequest categoriaRequest;
	private CategoriaEdicaoRequest categoriaEdicaoRequest;

	@BeforeEach
	void setup() {
		categoria = DataHelper.createCategoria();
		categoriaRequest = DataHelper.createCategoriaRequest();
		categoriaEdicaoRequest = DataHelper.createCategoriaEdicaoRequest();
	}

	@Test
	void deveriaSalvarCategoria() {
		when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

		CategoriaResponse categoriaSalva = categoriaService.salvarCategoria(categoriaRequest);

		assertNotNull(categoriaSalva);
		assertEquals("Eletrônicos", categoriaSalva.getNome());
		verify(categoriaRepository, times(1)).save(any(Categoria.class));
	}

	@Test
	void deveriaListarCategorias() {
		List<Categoria> categorias = DataHelper.createCategoriaList();
		when(categoriaRepository.findAll()).thenReturn(categorias);

		List<CategoriaResponse> resultado = categoriaService.listaCategorias();

		assertNotNull(resultado);
		assertEquals(3, resultado.size());
		assertEquals("Eletrônicos", resultado.get(0).getNome());
	}

	@Test
	void deveriaLancarErroCategoriaNaoEncontrada() {
		when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

		APIException exception = assertThrows(APIException.class, () -> categoriaService.buscarCategoriaPorId(99L));

		assertEquals(HttpStatus.NOT_FOUND, exception.getStatusException());
		assertEquals("Categoria não encontrada", exception.getMessage());
	}

	@Test
	void deveriaExcluirCategoriaSemProdutosAssociados() {
		when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
		when(produtoRepository.existsByCategoria(categoria)).thenReturn(false);

		categoriaService.excluirCategoria(1L);

		verify(categoriaRepository, times(1)).delete(categoria);
	}

	@Test
	void deveriaLancarErroAoExcluirCategoriaComProdutosAssociados() {
		when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
		when(produtoRepository.existsByCategoria(categoria)).thenReturn(true); 

		APIException exception = assertThrows(APIException.class, () -> categoriaService.excluirCategoria(1L));

		assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusException());
		assertEquals("Não é possível excluir a categoria pois há produtos associados a ela.", exception.getMessage());

		verify(categoriaRepository, never()).delete(any());
	}

	@Test
	void deveriaLancarErroAoExcluirCategoriaInexistente() {
		when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

		APIException exception = assertThrows(APIException.class, () -> categoriaService.excluirCategoria(99L));

		assertEquals(HttpStatus.NOT_FOUND, exception.getStatusException());
		assertEquals("Categoria não encontrada", exception.getMessage());
	}

	@Test
	void deveriaLancarErroExcluirCategoriaInexistente() {
		when(categoriaRepository.findById(anyLong())).thenReturn(Optional.empty());

		APIException exception = assertThrows(APIException.class, () -> categoriaService.excluirCategoria(99L));

		assertEquals(HttpStatus.NOT_FOUND, exception.getStatusException());
		assertEquals("Categoria não encontrada", exception.getMessage());
	}

	@Test
	void deveriaAtualizarCategoria() {
		when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
		when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

		CategoriaResponse categoriaAtualizada = categoriaService.atualizarCategoria(1L, categoriaEdicaoRequest);

		assertNotNull(categoriaAtualizada);
		assertEquals("Casa & Decoração", categoriaAtualizada.getNome());
		verify(categoriaRepository, times(1)).save(any(Categoria.class));
	}

	@Test
	void deveriaLancarErroAtualizarCategoriaInexistente() {
		when(categoriaRepository.findById(anyLong())).thenReturn(Optional.empty());

		APIException exception = assertThrows(APIException.class,
				() -> categoriaService.atualizarCategoria(99L, categoriaEdicaoRequest));

		assertEquals(HttpStatus.NOT_FOUND, exception.getStatusException());
		assertEquals("Categoria não encontrada", exception.getMessage());
	}

	@Test
	void deveriaObterCategoriaPorNome() {
		when(categoriaRepository.findByNome("Eletrônicos")).thenReturn(Optional.of(categoria));

		CategoriaResponse categoriaEncontrada = categoriaService.obterCategoriaPorNome("Eletrônicos");

		assertNotNull(categoriaEncontrada);
		assertEquals("Eletrônicos", categoriaEncontrada.getNome());

		verify(categoriaRepository, times(1)).findByNome("Eletrônicos");
	}

	@Test
	void deveriaLancarErroAoObterCategoriaPorNomeNaoEncontrado() {
		when(categoriaRepository.findByNome("Inexistente")).thenReturn(Optional.empty());

		APIException exception = assertThrows(APIException.class,
				() -> categoriaService.obterCategoriaPorNome("Inexistente"));

		assertEquals(HttpStatus.NOT_FOUND, exception.getStatusException());
		assertEquals("Categoria não encontrada", exception.getMessage());

		verify(categoriaRepository, times(1)).findByNome("Inexistente");
	}

	@Test
	void deveriaBuscarCategoriaPorId() {
		when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

		CategoriaResponse resultado = categoriaService.buscarCategoriaPorId(1L);

		assertNotNull(resultado);
		assertEquals("Eletrônicos", resultado.getNome());
	}

	@Test
	void deveriaLancarErroAoBuscarCategoriaPorIdNaoEncontrado() {
		when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

		APIException exception = assertThrows(APIException.class, () -> categoriaService.buscaCategoriaPorId(99L));

		assertEquals(HttpStatus.NOT_FOUND, exception.getStatusException());
		assertEquals("Categoria não encontrada", exception.getMessage());

		verify(categoriaRepository, times(1)).findById(99L);
	}
}
