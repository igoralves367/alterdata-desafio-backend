package br.com.alterdata.vendas.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.alterdata.vendas.security.jwt.JwtAuthFilter;
import br.com.alterdata.vendas.security.jwt.JwtService;
import br.com.alterdata.vendas.service.UserService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	
	 @Autowired
	    private UserService userService;

	    @Autowired
	    private JwtService jwtService;

	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }

	    @Bean
	    public OncePerRequestFilter jwtFilter() {
	        return new JwtAuthFilter(jwtService, userService);
	    }

	    @Override
	    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	        auth.userDetailsService(userService)
	                .passwordEncoder(passwordEncoder());
	    }

	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http
	            .csrf().disable()
	            .authorizeRequests(authorizeRequests ->
	                authorizeRequests
	                    .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
	                    .antMatchers(HttpMethod.POST, "/usuarios/**").permitAll()
	                    .antMatchers("/produtos/**").hasAnyRole(RoleConstants.ADMIN, RoleConstants.USER)
	                    .antMatchers("/api/categorias/nome").hasRole(RoleConstants.ADMIN)
	                    .antMatchers("/api/categorias/**").hasAnyRole(RoleConstants.ADMIN, RoleConstants.USER)
	                    .antMatchers("/usuarios/all").hasRole(RoleConstants.ADMIN)
	                    .anyRequest().authenticated()
	            )
	            .sessionManagement(sessionManagement ->
	                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	            )
	            .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
	    }
}
