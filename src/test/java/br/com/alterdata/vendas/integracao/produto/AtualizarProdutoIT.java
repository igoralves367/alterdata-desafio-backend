package br.com.alterdata.vendas.integracao.produto;

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
public class AtualizarProdutoIT {

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
    @DisplayName("Deveria atualizar um produto existente")
    void deveriaAtualizarProduto() throws Exception {
        String produtoAtualizadoJson = "{"
                + "\"nome\": \"Motosserra Profissional Gasolina\","
                + "\"descricao\": \"Modelo atualizado com maior potência\","
                + "\"referencia\": \"TCS53H20\","
                + "\"valorUnitario\": 450.00"
                + "}";

        mockMvc.perform(put("/produtos/atualizaProduto/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(produtoAtualizadoJson)
                .with(user("admin").password("senha123").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Motosserra Profissional Gasolina"))
                .andExpect(jsonPath("$.valorUnitario").value(450.00));
    }
}