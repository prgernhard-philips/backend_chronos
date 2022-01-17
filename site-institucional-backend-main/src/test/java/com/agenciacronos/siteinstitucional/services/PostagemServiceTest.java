package com.agenciacronos.siteinstitucional.services;

import com.agenciacronos.siteinstitucional.Repositories.PostagemRepository;
import com.agenciacronos.siteinstitucional.Services.PostagemService;
import com.agenciacronos.siteinstitucional.exceptions.PostagemNaoEncontradoException;
import com.agenciacronos.siteinstitucional.models.Postagem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class PostagemServiceTest {

    @Autowired
    private PostagemService postagemService;

    @MockBean
    private PostagemRepository postagemRepository;


    private Postagem postagem = new Postagem(1, "TituloPostagem", "Conteudo", "Imagem", LocalDate.now());


    @Test
    public void testarMetodoSalvarPostagemCaminhoPositivo(){
        Postagem postagem = new Postagem();
        Mockito.when(postagemRepository.save(Mockito.any(Postagem.class)))
                .thenReturn(postagem);
        Postagem objetoDeTeste = postagemService.salvarPostagem(postagem);
        Assertions.assertEquals(postagem, objetoDeTeste);
    }


    @Test
    public void testarMetodoListarTodosAsPostagensCaminhoPositivo(){
        Postagem postagem = new Postagem();
        Iterable<Postagem> postagemIterable = Arrays.asList(postagem);
        Mockito.when(postagemRepository.findAll()).thenReturn((List<Postagem>) postagemIterable);
        Assertions.assertTrue(postagemService.listarPostagens() instanceof List);
    }

    @Test
    public void testarMetodoListarTodasAsPostagensCaminhoPositivoBuscaPeloTitulo(){
        Postagem postagem = new Postagem();
        List<Postagem> postagemList= Arrays.asList(postagem);
        Mockito.when(postagemRepository.findByTituloContainingIgnoreCase(Mockito.anyString()))
                .thenReturn(postagemList);
        Assertions.assertEquals(postagemList, postagemService.listarPostagemPorTitulo("titulo"));
    }

    @Test
    public void testarMetodoListarPostagemPorIDCaminhoPositivo(){
        Postagem postagem = new Postagem();
        Optional<Postagem> postagemOptional = Optional.of(postagem);
        Mockito.when(postagemRepository.findById(Mockito.anyLong())).thenReturn(postagemOptional);
        Assertions.assertEquals(postagemOptional, postagemService.listarPostagemPorId(12));
    }

    @Test
    public void testarDeletarPostagemPorIDCaminhoPositivo() {
        Mockito.when(postagemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(postagem));
        postagemService.deletarPostagem(1);
        Mockito.verify(postagemRepository).delete(postagem);
    }

    @Test
    public void testrarDeletarPostagemPorIDCaminhoNegativo() {
        PostagemNaoEncontradoException postagemNaoEncontradoException = new PostagemNaoEncontradoException(postagem.getId());
        Mockito.when(postagemRepository.findById(Mockito.anyLong()))
                .thenThrow(postagemNaoEncontradoException);
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            postagemService.deletarPostagem(10);
        });
        Assertions.assertNotNull(exception);

    }



}
