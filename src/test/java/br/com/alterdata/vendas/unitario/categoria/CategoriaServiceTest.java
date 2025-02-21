package br.com.alterdata.vendas.unitario.categoria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import br.com.alterdata.vendas.handler.APIException;
import br.com.alterdata.vendas.model.Categoria;
import br.com.alterdata.vendas.repository.CategoriaRepository;
import br.com.alterdata.vendas.service.CategoriaService;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CategoriaService categoriaService;

    private Categoria categoria;
    private CategoriaDTO categoriaDTO;

    @BeforeEach
    void setup() {
        categoria = DataHelper.createCategoria();
        categoriaDTO = DataHelper.createCategoriaDTO();
    }

    @Test
    @DisplayName("Deveria salvar uma nova categoria")
    void deveriaSalvarCategoria() {
        when(categoriaRepository.existsByNomeIgnoreCase(anyString())).thenReturn(false);
        when(modelMapper.map(categoriaDTO, Categoria.class)).thenReturn(categoria);
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);
        when(modelMapper.map(categoria, CategoriaDTO.class)).thenReturn(categoriaDTO);

        CategoriaDTO categoriaSalva = categoriaService.salvarCategoria(categoriaDTO);

        assertNotNull(categoriaSalva);
        assertEquals("Eletrônicos", categoriaSalva.getNome());
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    @DisplayName("Deveria lançar erro ao tentar salvar uma categoria já existente")
    void deveriaLancarErroCategoriaJaCadastrada() {
        when(categoriaRepository.existsByNomeIgnoreCase(anyString())).thenReturn(true);

        APIException exception = assertThrows(APIException.class, () -> categoriaService.salvarCategoria(categoriaDTO));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusException());
        assertEquals("Categoria já cadastrada.", exception.getMessage());
        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    @DisplayName("Deveria obter ou criar uma nova categoria")
    void deveriaObterOuCriarCategoria() {
        when(categoriaRepository.findByNome(anyString())).thenReturn(null);
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        Categoria categoriaCriada = categoriaService.obterOuCriarCategoria("Eletrônicos");

        assertNotNull(categoriaCriada);
        assertEquals("Eletrônicos", categoriaCriada.getNome());
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    @DisplayName("Deveria obter uma categoria existente")
    void deveriaObterCategoriaExistente() {
        when(categoriaRepository.findByNome(anyString())).thenReturn(categoria);
        when(modelMapper.map(categoria, CategoriaDTO.class)).thenReturn(categoriaDTO);

        CategoriaDTO categoriaEncontrada = categoriaService.obterCategoriaPorNome("Eletrônicos");

        assertNotNull(categoriaEncontrada);
        assertEquals("Eletrônicos", categoriaEncontrada.getNome());
    }

    @Test
    @DisplayName("Deveria lançar erro ao tentar obter categoria inexistente")
    void deveriaLancarErroCategoriaNaoEncontrada() {
        when(categoriaRepository.findByNome(anyString())).thenReturn(null);

        APIException exception = assertThrows(APIException.class, () -> categoriaService.obterCategoriaPorNome("Inexistente"));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusException());
        assertEquals("Categoria não encontrada", exception.getMessage());
    }

    @Test
    @DisplayName("Deveria listar todas as categorias")
    void deveriaListarCategorias() {
        when(categoriaRepository.findAll()).thenReturn(List.of(categoria));
        when(modelMapper.map(categoria, CategoriaDTO.class)).thenReturn(categoriaDTO);

        List<CategoriaDTO> categorias = categoriaService.listaCategorias();

        assertNotNull(categorias);
        assertEquals(1, categorias.size());
        assertEquals("Eletrônicos", categorias.get(0).getNome());
    }

    @Test
    @DisplayName("Deveria buscar uma categoria por ID")
    void deveriaBuscarCategoriaPorId() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(modelMapper.map(categoria, CategoriaDTO.class)).thenReturn(categoriaDTO);

        CategoriaDTO categoriaEncontrada = categoriaService.buscarCategoriaPorId(1L);

        assertNotNull(categoriaEncontrada);
        assertEquals("Eletrônicos", categoriaEncontrada.getNome());
    }

    @Test
    @DisplayName("Deveria lançar erro ao buscar categoria por ID inexistente")
    void deveriaLancarErroCategoriaPorIdNaoEncontrada() {
        when(categoriaRepository.findById(anyLong())).thenReturn(Optional.empty());

        APIException exception = assertThrows(APIException.class, () -> categoriaService.buscarCategoriaPorId(99L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusException());
        assertEquals("Categoria não encontrada", exception.getMessage());
    }

    @Test
    @DisplayName("Deveria excluir uma categoria existente")
    void deveriaExcluirCategoria() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        categoriaService.excluirCategoria(1L);

        verify(categoriaRepository, times(1)).delete(categoria);
    }

    @Test
    @DisplayName("Deveria lançar erro ao excluir categoria inexistente")
    void deveriaLancarErroExcluirCategoriaInexistente() {
        when(categoriaRepository.findById(anyLong())).thenReturn(Optional.empty());

        APIException exception = assertThrows(APIException.class, () -> categoriaService.excluirCategoria(99L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusException());
        assertEquals("Categoria não encontrada", exception.getMessage());
    }

    @Test
    @DisplayName("Deveria atualizar uma categoria existente")
    void deveriaAtualizarCategoria() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);
        when(modelMapper.map(categoria, CategoriaDTO.class)).thenReturn(categoriaDTO);

        CategoriaDTO categoriaAtualizada = categoriaService.atualizarCategoria(1L, "Nova Categoria");

        assertNotNull(categoriaAtualizada);
        assertEquals("Nova Categoria", categoria.getNome());
        verify(categoriaRepository, times(1)).save(categoria);
    }

    @Test
    @DisplayName("Deveria lançar erro ao tentar atualizar categoria inexistente")
    void deveriaLancarErroAtualizarCategoriaInexistente() {
        when(categoriaRepository.findById(anyLong())).thenReturn(Optional.empty());

        APIException exception = assertThrows(APIException.class, () -> categoriaService.atualizarCategoria(99L, "Nova Categoria"));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusException());
        assertEquals("Categoria não encontrada", exception.getMessage());
    }
}
