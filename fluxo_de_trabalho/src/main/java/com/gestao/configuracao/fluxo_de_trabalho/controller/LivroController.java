package com.gestao.configuracao.fluxo_de_trabalho.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestao.configuracao.fluxo_de_trabalho.model.Livro;
import com.gestao.configuracao.fluxo_de_trabalho.repository.LivroRepository;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

	private final LivroRepository livroRepository;
	
	public LivroController(LivroRepository livroRepository) {
		this.livroRepository = livroRepository;
	}
	
	@GetMapping
	public List<Livro> getLivros(){
		return livroRepository.findAll();
	}
	
	@PostMapping
	public Livro addLivro(@RequestBody Livro livro) {
		return livroRepository.save(livro);
	}
	
	@DeleteMapping("/{isbn}")
	public ResponseEntity<Void> deletarLivro(@PathVariable Long isbn){
		Optional<Livro> livro = livroRepository.findById(isbn);
		
		if(livro.isEmpty()) {
			return ResponseEntity.notFound().build();//retorna 404
		}
		
		livroRepository.deleteById(isbn);
		return ResponseEntity.noContent().build();//retorna 204
	}
	
}