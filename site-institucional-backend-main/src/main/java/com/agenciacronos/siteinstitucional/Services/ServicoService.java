package com.agenciacronos.siteinstitucional.Services;

import com.agenciacronos.siteinstitucional.Repositories.ServicoRepository;
import com.agenciacronos.siteinstitucional.exceptions.ServicoNaoEncontradoException;
import com.agenciacronos.siteinstitucional.models.Servico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ServicoService {

    private final ServicoRepository servicoRepository;

    @Autowired
    public ServicoService(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    public Servico salvarServico(Servico servico){
        return servicoRepository.save(servico);
    }

    public List<Servico> listarServicos(){
        return servicoRepository.findAll();
    }

    public Optional<Servico> listarServicoPorId(long id){
        return servicoRepository.findById(id);
    }

    public List<Servico> listarServicoPorTitulo(String titulo){
        return servicoRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public void atualizarServico(Servico servicoAtualizada, long id){
        listarServicoPorId(id)
                .map(servico -> {
                    servico.setTitulo(servicoAtualizada.getTitulo());
                    servico.setDescricao(servicoAtualizada.getDescricao());
                    servico.setImgUrl(servicoAtualizada.getImgUrl());
                    servico.setDataCadastro(LocalDate.now());
                    return servicoRepository.save(servico);
                }).orElseThrow(()-> new ServicoNaoEncontradoException(id));
    }

    public void deletarServico(long id){
        listarServicoPorId(id)
                .map(servico -> {
                    servicoRepository.delete(servico);
                    return Void.TYPE;
                }).orElseThrow(()->new ServicoNaoEncontradoException(id));
    }

}
