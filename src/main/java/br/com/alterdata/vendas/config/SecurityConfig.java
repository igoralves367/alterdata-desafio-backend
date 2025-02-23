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
	            .authorizeRequests()
	            
	            .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
	            
	            .antMatchers(HttpMethod.POST, "/usuarios/**").permitAll()
	            .antMatchers("/usuarios/all").hasRole(RoleConstants.ADMIN)

	            .antMatchers(HttpMethod.POST, "/produtos").hasRole(RoleConstants.ADMIN)
	            .antMatchers(HttpMethod.PUT, "/produtos/**").hasRole(RoleConstants.ADMIN)
	            .antMatchers(HttpMethod.DELETE, "/produtos/**").hasRole(RoleConstants.ADMIN)
	            .antMatchers(HttpMethod.GET, "/produtos/**").hasAnyRole(RoleConstants.ADMIN, RoleConstants.USER)

	            .antMatchers("/produtos/pesquisa").hasRole(RoleConstants.ADMIN)

	            .antMatchers(HttpMethod.POST, "/categorias").hasRole(RoleConstants.ADMIN)
	            .antMatchers(HttpMethod.PUT, "/categorias/**").hasRole(RoleConstants.ADMIN)
	            .antMatchers(HttpMethod.DELETE, "/categorias/**").hasRole(RoleConstants.ADMIN)
	            .antMatchers(HttpMethod.GET, "/categorias/**").hasAnyRole(RoleConstants.ADMIN, RoleConstants.USER)

	            .anyRequest().authenticated()
	            .and()
	            .sessionManagement()
	            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	            .and()
	            .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
	    }

}
