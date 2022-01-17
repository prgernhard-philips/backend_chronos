package com.agenciacronos.siteinstitucional.Services;

import com.agenciacronos.siteinstitucional.Repositories.IntegranteRepository;
import com.agenciacronos.siteinstitucional.SiteInstitucionalApplication;
import com.agenciacronos.siteinstitucional.exceptions.IntegranteNaoEncontradoException;
import com.agenciacronos.siteinstitucional.models.Integrante;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class IntegranteService {

    private final IntegranteRepository integranteRepository;

    private static Logger logger = LoggerFactory.getLogger(SiteInstitucionalApplication.class);

    @Autowired
    public IntegranteService(IntegranteRepository integranteRepository) {
        this.integranteRepository = integranteRepository;
    }

    public Integrante salvarIntegrante(Integrante integrante){
        return integranteRepository.save(integrante);
    }

    public List<Integrante> listarIntegrantes(){
        return integranteRepository.findAll();
    }

    public Optional<Integrante> listarIntegrantePorId(long id){
        return integranteRepository.findById(id);
    }

    public List<Integrante> listarIntegrantePorNome(String nome){
        return integranteRepository.findByNomeContainingIgnoreCase(nome);
    }

    public void atualizarIntegrante(Integrante integranteAtualizada, long id){
        listarIntegrantePorId(id)
                .map(integrante -> {
                    integrante.setNome(integranteAtualizada.getNome());
                    integrante.setFuncao(integranteAtualizada.getFuncao());
                    integrante.setLinkedin(integranteAtualizada.getLinkedin());
                    integrante.setGithub(integranteAtualizada.getGithub());
                    integrante.setImgPerfil(integranteAtualizada.getImgPerfil());
                    integrante.setDataCadastro(LocalDate.now());
                    return integranteRepository.save(integrante);
                }).orElseThrow(()-> new IntegranteNaoEncontradoException(id));
    }

    public void deletarIntegrante(long id){
        listarIntegrantePorId(id)
                .map(integrante -> {
                    integranteRepository.delete(integrante);
                    logger.info("Integrante com id: "+id+" deletado com sucesso" + integrante.toString());
                    return Void.TYPE;
                }).orElseThrow(()->new IntegranteNaoEncontradoException(id));
    }



}
