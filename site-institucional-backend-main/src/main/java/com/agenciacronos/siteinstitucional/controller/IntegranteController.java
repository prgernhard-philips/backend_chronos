package com.agenciacronos.siteinstitucional.controller;

import com.agenciacronos.siteinstitucional.Services.IntegranteService;
import com.agenciacronos.siteinstitucional.SiteInstitucionalApplication;
import com.agenciacronos.siteinstitucional.exceptions.IntegranteNaoEncontradoException;
import com.agenciacronos.siteinstitucional.models.Integrante;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/v1/integrants")
@RestController
@CrossOrigin("*")
public class IntegranteController {

    private final IntegranteService integranteService;

    private static Logger logger = LoggerFactory.getLogger(SiteInstitucionalApplication.class);

    @Autowired
    public IntegranteController(IntegranteService integranteService) {
        this.integranteService = integranteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Integrante salvarCategoria(@Valid @RequestBody Integrante integrante){
        return integranteService.salvarIntegrante(integrante);
    }

    @GetMapping("/{id}")
    public Integrante listarIntegrantePorId(@PathVariable long id){
        return integranteService
                .listarIntegrantePorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Integrante não encontrado"));
    }

    @GetMapping
    public List<Integrante> listarTodosIntegrantes() {
        return integranteService.listarIntegrantes();
    }

    @GetMapping("/names/{nome}")
    public List<Integrante> listarIntegrantePorNome(@PathVariable String nome){
        return integranteService.listarIntegrantePorNome(nome);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizarIntegrante(@Valid @RequestBody Integrante integrante, @PathVariable long id) {
        try {
            integranteService.atualizarIntegrante(integrante, id);

            logger.info("Integrante com id: "+id+" encontrado");

        } catch (IntegranteNaoEncontradoException ex) {

            logger.info("Integrante com id: "+id+" não encontrada");

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarIntegrantePorId(@PathVariable Integer id){
        try {
            integranteService.deletarIntegrante(id);
            logger.info("Integrante com id: "+id+" encontrado");
        } catch (IntegranteNaoEncontradoException ex){
            logger.info("Integrante com id: "+id+" não encontrada");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

    }


}
