package com.agenciacronos.siteinstitucional.controller;

import com.agenciacronos.siteinstitucional.Services.PostagemService;
import com.agenciacronos.siteinstitucional.SiteInstitucionalApplication;
import com.agenciacronos.siteinstitucional.exceptions.PostagemNaoEncontradoException;
import com.agenciacronos.siteinstitucional.models.Postagem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/v1/posts")
@RestController
@CrossOrigin("*")
public class PostagemController {
    private final PostagemService postagemService;
    private static Logger logger = LoggerFactory.getLogger(SiteInstitucionalApplication.class);

    @Autowired
    public PostagemController(PostagemService postagemService) {
        this.postagemService = postagemService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Postagem salvarPostagem(@Valid @RequestBody Postagem postagem) {
        return postagemService.salvarPostagem(postagem);
    }

    @GetMapping("/{id}")
    public Postagem listarPostagemPorId(@PathVariable long id) {
        return postagemService
                .listarPostagemPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Postagem não encontrado"));
    }

    @GetMapping
    public List<Postagem> listarTodasPostagens() {
        return postagemService.listarPostagens();
    }

    @GetMapping("/titles/{titulo}")
    public List<Postagem> listarPostagemPorNome(@PathVariable String titulo) {
        return postagemService.listarPostagemPorTitulo(titulo);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizarPostagem(@Valid @RequestBody Postagem postagem, @PathVariable long id) {
        try {
            postagemService.atualizarPostagem(postagem, id);
            logger.info("Postagem com id: "+id+" encontrado");
        } catch (PostagemNaoEncontradoException ex) {
            logger.info("Postagem com id: "+id+" não encontrada");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarPostagemPorId(@PathVariable Integer id) {
        try {
            postagemService.deletarPostagem(id);
            logger.info("Postagem com id: "+id+" encontrado");
        } catch (PostagemNaoEncontradoException ex) {
            logger.info("Postagem com id: "+id+" não encontrada");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

    }

}