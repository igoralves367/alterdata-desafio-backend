package br.com.alterdata.vendas.unitario.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.alterdata.vendas.DataHelper;
import br.com.alterdata.vendas.dto.LoginDTO;
import br.com.alterdata.vendas.dto.UserDTO;
import br.com.alterdata.vendas.handler.APIException;
import br.com.alterdata.vendas.model.User;
import br.com.alterdata.vendas.repository.UserRepository;
import br.com.alterdata.vendas.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;
    private LoginDTO loginDTO;

    @BeforeEach
    void setup() {
        user = DataHelper.createUser();
        userDTO = DataHelper.createUserDTO();
        loginDTO = DataHelper.createLoginDTO();
    }

    @Test
    @DisplayName("Deveria salvar um novo usuário")
    void deveriaSalvarUsuario() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        UserDTO usuarioSalvo = userService.save(userDTO);

        assertNotNull(usuarioSalvo);
        assertEquals("admin", usuarioSalvo.getLogin());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deveria carregar um usuário pelo login")
    void deveriaCarregarUsuarioPorLogin() {
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername("admin");

        assertNotNull(userDetails);
        assertEquals("admin", userDetails.getUsername());
        assertEquals(2, userDetails.getAuthorities().size()); // ADMIN e USER
    }

    @Test
    @DisplayName("Deveria lançar erro ao tentar carregar usuário inexistente")
    void deveriaLancarErroUsuarioNaoEncontrado() {
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.empty());

        APIException exception = assertThrows(APIException.class, () -> userService.loadUserByUsername("inexistente"));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusException());
        assertEquals("Login não encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("Deveria autenticar um usuário com senha válida")
    void deveriaAutenticarUsuario() {
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(user));
        when(encoder.matches(anyString(), anyString())).thenReturn(true);

        UserDetails authenticatedUser = userService.authenticate(loginDTO);

        assertNotNull(authenticatedUser);
        assertEquals("admin", authenticatedUser.getUsername());
    }

    @Test
    @DisplayName("Deveria lançar erro ao tentar autenticar usuário com senha inválida")
    void deveriaLancarErroSenhaInvalida() {
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(user));
        when(encoder.matches(anyString(), anyString())).thenReturn(false);

        APIException exception = assertThrows(APIException.class, () -> userService.authenticate(loginDTO));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusException());
        assertEquals("Senha inválida", exception.getMessage());
    }

    @Test
    @DisplayName("Deveria listar todos os usuários")
    void deveriaListarUsuarios() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        List<UserDTO> users = userService.findAll();

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("admin", users.get(0).getLogin());
    }

    @Test
    @DisplayName("Deveria lançar erro ao tentar listar usuários inexistentes")
    void deveriaLancarErroListaVazia() {
        when(userRepository.findAll()).thenReturn(List.of());

        APIException exception = assertThrows(APIException.class, () -> userService.findAll());

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusException());
        assertEquals("Usuário não encontrado", exception.getMessage());
    }
}
