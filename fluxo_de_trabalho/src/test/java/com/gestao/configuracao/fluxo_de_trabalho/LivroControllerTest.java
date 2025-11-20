package com.gestao.configuracao.fluxo_de_trabalho;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.gestao.configuracao.fluxo_de_trabalho.model.Livro;
import com.gestao.configuracao.fluxo_de_trabalho.repository.LivroRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class LivroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LivroRepository livroRepository;

    @BeforeEach
    void setup() {
        livroRepository.deleteAll();
    }

    // ---------------------- TESTE POST ----------------------
    @Test
    void deveAdicionarLivro() throws Exception {
        String json = """
            {
                "titulo": "Novo Livro",
                "autor": "Autor Teste",
                "dataLancamento": "2024-01-01"
            }
            """;

        mockMvc.perform(post("/api/livros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()) // POST retorna 200 no seu controller
                .andExpect(jsonPath("$.isbn").exists())
                .andExpect(jsonPath("$.titulo").value("Novo Livro"))
                .andExpect(jsonPath("$.autor").value("Autor Teste"));
    }

    // ---------------------- TESTE GET LISTA ----------------------
    @Test
    void deveListarLivros() throws Exception {
        livroRepository.save(new Livro("Livro 1", "Autor 1", "2024-01-01"));
        livroRepository.save(new Livro("Livro 2", "Autor 2", "2024-01-02"));

        mockMvc.perform(get("/api/livros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // ---------------------- TESTE DELETE EXISTENTE ----------------------
    @Test
    void deveDeletarLivroExistente() throws Exception {
        Livro livro = new Livro("Teste", "Autor", "2024-05-01");
        livroRepository.save(livro);

        mockMvc.perform(delete("/api/livros/" + livro.getIsbn()))
                .andExpect(status().isNoContent());
    }

    // ---------------------- TESTE DELETE INEXISTENTE ----------------------
    @Test
    void deveRetornar404AoDeletarLivroInexistente() throws Exception {
        mockMvc.perform(delete("/api/livros/9999"))
                .andExpect(status().isNotFound());
    }

}
