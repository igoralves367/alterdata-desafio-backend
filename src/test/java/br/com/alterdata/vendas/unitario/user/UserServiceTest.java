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
import br.com.alterdata.vendas.dto.LoginRequest;
import br.com.alterdata.vendas.dto.UserRequest;
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
    private UserRequest userDTO;
    private LoginRequest loginDTO;

    @BeforeEach
    void setup() {
        user = DataHelper.createUser();
        userDTO = DataHelper.createUserDTO();
        loginDTO = DataHelper.createLoginDTO();
    }

    @Test
    void deveriaSalvarUsuario() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(modelMapper.map(user, UserRequest.class)).thenReturn(userDTO);

        UserRequest usuarioSalvo = userService.save(userDTO);

        assertNotNull(usuarioSalvo);
        assertEquals("admin", usuarioSalvo.getLogin());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void deveriaCarregarUsuarioPorLogin() {
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername("admin");

        assertNotNull(userDetails);
        assertEquals("admin", userDetails.getUsername());
        assertEquals(2, userDetails.getAuthorities().size());
    }

    @Test
    void deveriaLancarErroUsuarioNaoEncontrado() {
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.empty());

        APIException exception = assertThrows(APIException.class, () -> userService.loadUserByUsername("inexistente"));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusException());
        assertEquals("Login não encontrado", exception.getMessage());
    }

    @Test
    void deveriaAutenticarUsuario() {
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(user));
        when(encoder.matches(anyString(), anyString())).thenReturn(true);

        UserDetails authenticatedUser = userService.authenticate(loginDTO);

        assertNotNull(authenticatedUser);
        assertEquals("admin", authenticatedUser.getUsername());
    }

    @Test
    void deveriaLancarErroSenhaInvalida() {
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(user));
        when(encoder.matches(anyString(), anyString())).thenReturn(false);

        APIException exception = assertThrows(APIException.class, () -> userService.authenticate(loginDTO));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusException());
        assertEquals("Senha inválida", exception.getMessage());
    }

    @Test
    void deveriaListarUsuarios() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(modelMapper.map(user, UserRequest.class)).thenReturn(userDTO);

        List<UserRequest> users = userService.findAll();

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("admin", users.get(0).getLogin());
    }

    @Test
    void deveriaLancarErroListaVazia() {
        when(userRepository.findAll()).thenReturn(List.of());

        APIException exception = assertThrows(APIException.class, () -> userService.findAll());

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusException());
        assertEquals("Usuário não encontrado", exception.getMessage());
    }
}
