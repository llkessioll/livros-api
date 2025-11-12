package com.gestao.configuracao.fluxo_de_trabalho;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

    private Livro livro;

    @BeforeEach
    void setup() {
        livroRepository.deleteAll();

        livro = new Livro();
        livro.setTitulo("Teste de Remoção");
        livro.setAutor("Autor Exemplo");
        livro.setDataLancamento("2024-05-01");

        livroRepository.save(livro);
    }

    @Test
    void deveDeletarLivroExistente() throws Exception {
        mockMvc.perform(delete("/api/livros/" + livro.getIsbn())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()); // 204
    }

    @Test
    void deveRetornar404QuandoLivroNaoExiste() throws Exception {
        mockMvc.perform(delete("/livros/9999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // 404
    }
}