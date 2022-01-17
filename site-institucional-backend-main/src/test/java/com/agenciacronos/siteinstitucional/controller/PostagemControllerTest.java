package com.agenciacronos.siteinstitucional.controller;

import com.agenciacronos.siteinstitucional.Services.PostagemService;
import com.agenciacronos.siteinstitucional.exceptions.PostagemNaoEncontradoException;
import com.agenciacronos.siteinstitucional.models.Postagem;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;

//WebMvcTest é a anotação para realização de teste de controller, para ele precisa receber como parametro a classe que será testada
@WebMvcTest(PostagemController.class)
public class PostagemControllerTest {

    @MockBean
    private PostagemService postagemService;

    //MockMvc é a classe responsavel por simular requisoções HTTP no endpoint do controller
    @Autowired
    private MockMvc mockMvc;

    private Postagem postagem;
    private ObjectMapper objectMapper;

    private String URI = "/v1/posts";

    @BeforeEach
    void setUp(){

        postagem = new Postagem(1,"TituloPostagem","ConteudoPostagem","ImagemPostagem", LocalDate.now());
        postagem = new Postagem("TituloPostagem","ConteudoPostagem","ImagemPostagem");

        objectMapper = new ObjectMapper();
    }


    @Test
    void cadastrarPostagemTest() throws Exception {

        when(postagemService.salvarPostagem(Mockito.any(Postagem.class))).thenReturn(postagem);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postagem).getBytes(StandardCharsets.UTF_8))
                        .accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        Assertions.assertThat(result).isNotNull();
        String integranteJson = result.getResponse().getContentAsString();
        Assertions.assertThat(integranteJson).isNotEmpty();
        Assertions.assertThat(integranteJson).isEqualToIgnoringCase(objectMapper.writeValueAsString(postagem));
    }


    @Test
    public void testarBuscaDePostagemPorId() throws Exception{
        when(postagemService.listarPostagemPorId(Mockito.anyLong()))
                .thenReturn(java.util.Optional.ofNullable(postagem));

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(URI+"/2")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk());

        String jsonDeResposta = resultActions.andReturn().getResponse().getContentAsString();
        Postagem postagemObj = objectMapper.readValue(jsonDeResposta, Postagem.class);

    }

    @Test
    public void testarBuscaDePostagemPeloIdNaoExiste() throws Exception {
        when(postagemService.listarPostagemPorId(Mockito.anyInt()))
                .thenThrow(RuntimeException.class);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(URI+"/80")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void buscarTodasPostagens() throws Exception {

        List<Postagem> postagemList = List.of(postagem);
        when(postagemService.listarPostagens()).thenReturn(postagemList);
        this.mockMvc.perform(MockMvcRequestBuilders.get(URI))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(containsString("TituloPostagem")));
    }

    @Test
    public void buscarPostagensPorTitulo() throws Exception {

        List<Postagem> postagemList = List.of(postagem);
        when(postagemService.listarPostagemPorTitulo(Mockito.anyString())).thenReturn(postagemList);
        this.mockMvc.perform(MockMvcRequestBuilders.get(URI+"/titles/TituloPostagem"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(containsString("TituloPostagem")));
    }

    @Test
    public void deletarPostagemCaminhoPositivo() throws Exception {

        Mockito.when(postagemService.listarPostagemPorId(Mockito.anyLong()))
                .thenReturn(java.util.Optional.ofNullable(postagem));

        postagemService.deletarPostagem(1);

        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.delete(URI+"/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testarDeletarPostagemCaminhoNegativo() throws Exception {

        Mockito.doThrow(PostagemNaoEncontradoException.class).when(postagemService).deletarPostagem(Mockito.anyLong());

        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.delete(URI+"/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }



}
