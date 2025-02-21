package br.com.alterdata.vendas.integracao.produto;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
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
public class PesquisarProdutosIT {

    @Autowired
    private WebApplicationContext webAppContextSetup;

    private MockMvc mockMvc;
    
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContextSetup)
                .apply(springSecurity()) 
                .build();
    }
    
    @Sql("/seeds/produtos.sql")
    @Test
    void deveriaEncontrarProdutosPorPesquisa() throws Exception {
    	mockMvc.perform(get("/produtos/pesquisar/Poda")
    	        .contentType(MediaType.APPLICATION_JSON)
    	        .with(user("admin").password("senha123").roles("ADMIN")))
    	        .andDo(print()) 
    	        .andExpect(status().isOk())
    	        .andExpect(jsonPath("$.length()").value(1))
    	        .andExpect(jsonPath("$[0].nome").exists()); 
    }
    
    @Sql("/seeds/produtos.sql")
    @Test
    void deveriaRetornarErroQuandoCategoriaNaoExiste() throws Exception {
        mockMvc.perform(get("/produtos/categoria/CategoriaInexistente")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("admin").password("senha123").roles("ADMIN")))
                .andDo(print()) 
                .andExpect(status().isNotFound()); // Espera um 404
    }
}
