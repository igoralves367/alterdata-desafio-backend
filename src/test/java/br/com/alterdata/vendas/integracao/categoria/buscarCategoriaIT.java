package br.com.alterdata.vendas.integracao.categoria;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import br.com.alterdata.vendas.VendasApplication;

@SpringBootTest(classes = {VendasApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Tag("integracao")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class buscarCategoriaIT {
	
	    @Autowired
	    private WebApplicationContext webAppContextSetup;

	    private MockMvc mockMvc;

	    @BeforeEach
	    void setup() {
	        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContextSetup)
	                .apply(springSecurity()) 
	                .build();
	    }
	
	@Sql("/seeds/categorias.sql")
	@Test
	@DisplayName("Deveria buscar uma categoria por ID")
	void deveriaBuscarCategoriaPorId() throws Exception {
	    mockMvc.perform(get("/categorias/buscarProduto/1")
	            .contentType(MediaType.APPLICATION_JSON)
	            .with(user("admin").password("senha123").roles("ADMIN")))  
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.nome").value("Ferramentas Agrícolas"));
	}

	@Sql("/seeds/categorias.sql")
	@Test
	@DisplayName("Deveria retornar erro ao buscar uma categoria inexistente")
	void deveriaRetornarErroAoBuscarCategoriaInexistente() throws Exception {
	    mockMvc.perform(get("/categorias/buscarProduto/999")
	            .contentType(MediaType.APPLICATION_JSON)
	            .with(user("admin").password("senha123").roles("ADMIN")))  
	            .andExpect(status().isNotFound())
	            .andExpect(jsonPath("$.message").value("Categoria não encontrada"));
	}
}
