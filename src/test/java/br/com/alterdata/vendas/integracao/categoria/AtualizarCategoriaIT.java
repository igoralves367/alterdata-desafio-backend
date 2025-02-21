package br.com.alterdata.vendas.integracao.categoria;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
public class AtualizarCategoriaIT {
	
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
    @DisplayName("Deveria atualizar uma categoria existente")
    void deveriaAtualizarCategoria() throws Exception {
        String categoriaAtualizadaJson = "\"Jardinagem Avançada\""; 

        mockMvc.perform(put("/categorias/ataulizaCategoria/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(categoriaAtualizadaJson)
                .with(user("admin").password("senha123").roles("ADMIN")))  
                .andExpect(status().isOk());
    }

    @Sql("/seeds/categorias.sql")
    @Test
    @DisplayName("Deveria retornar erro ao tentar atualizar uma categoria inexistente")
    void deveriaRetornarErroAoAtualizarCategoriaInexistente() throws Exception {
        String categoriaAtualizadaJson = "\"Categoria Inexistente\"";

        mockMvc.perform(put("/categorias/ataulizaCategoria/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(categoriaAtualizadaJson)
                .with(user("admin").password("senha123").roles("ADMIN")))  
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Categoria não encontrada"));
    }
}
