package com.agenciacronos.siteinstitucional.Services;

import com.agenciacronos.siteinstitucional.Repositories.PostagemRepository;
import com.agenciacronos.siteinstitucional.exceptions.PostagemNaoEncontradoException;
import com.agenciacronos.siteinstitucional.models.Postagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PostagemService {

    private final PostagemRepository postagemRepository;

    @Autowired
    public PostagemService(PostagemRepository postagemRepository) {
        this.postagemRepository = postagemRepository;
    }

    public Postagem salvarPostagem(Postagem postagem){
        return postagemRepository.save(postagem);
    }

    public List<Postagem> listarPostagens(){
        return postagemRepository.findAll();
    }

    public Optional<Postagem> listarPostagemPorId(long id){
        return postagemRepository.findById(id);
    }

    public List<Postagem> listarPostagemPorTitulo(String titulo){
        return postagemRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public void atualizarPostagem(Postagem postagemAtualizada, long id){
        listarPostagemPorId(id)
                .map(postagem -> {
                    postagem.setTitulo(postagemAtualizada.getTitulo());
                    postagem.setPostagem(postagemAtualizada.getPostagem());
                    postagem.setImagem(postagemAtualizada.getImagem());
                    postagem.setDataCadastro(LocalDate.now());
                    return postagemRepository.save(postagem);
                }).orElseThrow(()-> new PostagemNaoEncontradoException(id));
    }

    public void deletarPostagem(long id){
        listarPostagemPorId(id)
                .map(postagem -> {
                    postagemRepository.delete(postagem);
                    return Void.TYPE;
                }).orElseThrow(()->new PostagemNaoEncontradoException(id));
    }

}
