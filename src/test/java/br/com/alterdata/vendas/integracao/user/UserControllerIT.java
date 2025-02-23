package br.com.alterdata.vendas.integracao.user;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import br.com.alterdata.vendas.VendasApplication;

@SpringBootTest(classes = {VendasApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Tag("integracao")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional 
public class UserControllerIT {

    @Autowired
    private WebApplicationContext webAppContextSetup;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContextSetup)
                .apply(springSecurity())
                .build();
    }

    @Test
    void deveriaCriarUsuario() throws Exception {
        String userJson = "{"
                + "\"login\": \"admin\","
                + "\"password\": \"senha123\""
                + "}";

        mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.login").value("admin"));
    }

    @Test
    void deveriaAutenticarUsuarioERetornarToken() throws Exception {
        String userJson = "{"
                + "\"login\": \"admin\","
                + "\"password\": \"senha123\""
                + "}";

        mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isCreated());

        String loginJson = "{"
                + "\"login\": \"admin\","
                + "\"password\": \"senha123\""
                + "}";

        mockMvc.perform(post("/usuarios/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists()); 
    }

    @Test
    void deveriaRetornarUsuariosCadastrados() throws Exception {
        String userJson = "{"
                + "\"login\": \"admin\","
                + "\"password\": \"senha123\""
                + "}";

        mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/usuarios/all")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("admin").password("senha123").roles("ADMIN")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1)) 
                .andExpect(jsonPath("$[0].login").value("admin"));
    }

    @Test
    void deveriaRetornarErroParaLoginInvalido() throws Exception {
        String loginJson = "{"
                + "\"login\": \"usuario_invalido\","
                + "\"password\": \"senhaerrada\""
                + "}";

        mockMvc.perform(post("/usuarios/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andDo(print())
                .andExpect(status().isNotFound()); 
    }

}
