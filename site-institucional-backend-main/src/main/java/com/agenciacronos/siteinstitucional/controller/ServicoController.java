package com.agenciacronos.siteinstitucional.controller;

import com.agenciacronos.siteinstitucional.Services.IntegranteService;
import com.agenciacronos.siteinstitucional.Services.ServicoService;
import com.agenciacronos.siteinstitucional.SiteInstitucionalApplication;
import com.agenciacronos.siteinstitucional.exceptions.IntegranteNaoEncontradoException;
import com.agenciacronos.siteinstitucional.exceptions.ServicoNaoEncontradoException;
import com.agenciacronos.siteinstitucional.models.Integrante;
import com.agenciacronos.siteinstitucional.models.Servico;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/v1/services")
@RestController
@CrossOrigin("*")
public class ServicoController {
    private final ServicoService servicoService;
    private static Logger logger = LoggerFactory.getLogger(SiteInstitucionalApplication.class);

    @Autowired
    public ServicoController(ServicoService servicoService) {
        this.servicoService = servicoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Servico salvarServico(@Valid @RequestBody Servico servico) {
        return servicoService.salvarServico(servico);
    }

    @GetMapping("/{id}")
    public Servico listarServicoPorId(@PathVariable long id) {
        return servicoService
                .listarServicoPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado"));
    }

    @GetMapping
    public List<Servico> listarTodosServicos() {
        return servicoService.listarServicos();
    }

    @GetMapping("/titles/{titulo}")
    public List<Servico> listarIntegrantePorNome(@PathVariable String titulo) {
        return servicoService.listarServicoPorTitulo(titulo);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizarServico(@Valid @RequestBody Servico servico, @PathVariable long id) {
        try {
            servicoService.atualizarServico(servico, id);
            logger.info("Serviço com id: "+id+" encontrado");
        } catch (ServicoNaoEncontradoException ex) {
            logger.info("Serviço com id: "+id+" não encontrado");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarServicoPorId(@PathVariable Integer id) {
        try {
            servicoService.deletarServico(id);
            logger.info("Serviço com id: "+id+" encontrado");
        } catch (ServicoNaoEncontradoException ex) {
            logger.info("Serviço com id: "+id+" não encontrado");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

    }

}