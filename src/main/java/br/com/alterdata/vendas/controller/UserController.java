package br.com.alterdata.vendas.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.alterdata.vendas.dto.LoginRequest;
import br.com.alterdata.vendas.dto.TokenResponse;
import br.com.alterdata.vendas.dto.UserRequest;
import br.com.alterdata.vendas.handler.excepition.InvalidPasswordException;
import br.com.alterdata.vendas.model.User;
import br.com.alterdata.vendas.security.jwt.JwtService;
import br.com.alterdata.vendas.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;

    private final PasswordEncoder encoder;

    private final JwtService jwtService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserRequest save(@RequestBody @Valid UserRequest user){
        String encrypted = encoder.encode(user.getPassword());
        user.setPassword(encrypted);
        return userService.save(user);
    }

    @PostMapping("/auth")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponse authenticate(@RequestBody @Valid LoginRequest dto){
        try {
            UserDetails authUser = userService.authenticate(dto);

            User user = User.builder()
                    .login(authUser.getUsername())
                    .password(authUser.getPassword())
                    .build();

            String token = jwtService.generateToken(user);
            return new TokenResponse(user.getLogin(), token);

        }catch (UsernameNotFoundException | InvalidPasswordException ex){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }
    } 

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<UserRequest> findAll(){
        return userService.findAll();
    }
}
