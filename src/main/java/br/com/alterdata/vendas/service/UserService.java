package br.com.alterdata.vendas.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.alterdata.vendas.config.RoleConstants;
import br.com.alterdata.vendas.dto.LoginDTO;
import br.com.alterdata.vendas.dto.UserDTO;
import br.com.alterdata.vendas.handler.APIException;
import br.com.alterdata.vendas.model.User;
import br.com.alterdata.vendas.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;

	private final ModelMapper modelMapper;

	private final PasswordEncoder encoder;

	@Transactional
	public UserDTO save(UserDTO dto) {
		User user;
		user = User.builder().login(dto.getLogin()).password(dto.getPassword()).admin(dto.isAdmin()).build();

		user = userRepository.save(user);
		return modelMapper.map(user, UserDTO.class);
	}

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		return userRepository.findByLogin(login).map(user -> {
			String[] roles = user.isAdmin() ? new String[] { RoleConstants.ADMIN, RoleConstants.USER }
					: new String[] { RoleConstants.USER };

			return org.springframework.security.core.userdetails.User.builder().username(user.getLogin())
					.password(user.getPassword()).roles(roles).build();
		}).orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Login não encontrado"));
	}

	public UserDetails authenticate(LoginDTO dto) {
		UserDetails loadedUser = loadUserByUsername(dto.getLogin());
		boolean match = encoder.matches(dto.getPassword(), loadedUser.getPassword());
		if (match) {
			return loadedUser;
		}
		throw APIException.build(HttpStatus.UNAUTHORIZED, "Senha inválida");
	}

	public List<UserDTO> findAll() {
		List<User> users = userRepository.findAll();

		if (!users.isEmpty()) {
			return users.stream().map(user -> modelMapper.map(user, UserDTO.class)).collect(Collectors.toList());
		} else {
			throw APIException.build(HttpStatus.NOT_FOUND, "Usuário não encontrado");
		}
	}

}
