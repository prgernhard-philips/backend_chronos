package com.agenciacronos.siteinstitucional.controller;

import com.agenciacronos.siteinstitucional.Services.ServicoService;
import com.agenciacronos.siteinstitucional.exceptions.ServicoNaoEncontradoException;
import com.agenciacronos.siteinstitucional.models.Servico;
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
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;

//WebMvcTest é a anotação para realização de teste de controller, para ele precisa receber como parametro a classe que será testada
@WebMvcTest(ServicoController.class)
public class ServicoControllerTest {

    @MockBean
    private ServicoService servicoService;

    //MockMvc é a classe responsavel por simular requisoções HTTP no endpoint do controller
    @Autowired
    private MockMvc mockMvc;

    private Servico servico;
    private ObjectMapper objectMapper;


    private String URI = "/v1/services";

    @BeforeEach
    void setUp(){

        servico = new Servico("Titulo", "Titulo", "Imagem");

        objectMapper = new ObjectMapper();
    }


    @Test
    void cadastrarServicoTest() throws Exception {

        when(servicoService.salvarServico(Mockito.any(Servico.class))).thenReturn(servico);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(servico).getBytes(StandardCharsets.UTF_8))
                        .accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        Assertions.assertThat(result).isNotNull();
        String integranteJson = result.getResponse().getContentAsString();
        Assertions.assertThat(integranteJson).isNotEmpty();
        Assertions.assertThat(integranteJson).isEqualToIgnoringCase(objectMapper.writeValueAsString(servico));
    }


    @Test
    public void testarBuscaDeServicoPorId() throws Exception{
        when(servicoService.listarServicoPorId(Mockito.anyLong()))
                .thenReturn(java.util.Optional.ofNullable(servico));

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(URI+"/4")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk());

        String jsonDeResposta = resultActions.andReturn().getResponse().getContentAsString();
        Servico servico2 = objectMapper.readValue(jsonDeResposta, Servico.class);

    }

    @Test
    public void testarBuScaDeServicoPeloIdNaoExiste() throws Exception {
        when(servicoService.listarServicoPorId(Mockito.anyInt()))
                .thenThrow(RuntimeException.class);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(URI+"/60")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void buscarTodosServicos() throws Exception {

        List<Servico> servicoList = List.of(servico);
        when(servicoService.listarServicos()).thenReturn(servicoList);
        this.mockMvc.perform(MockMvcRequestBuilders.get(URI))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(containsString("Titulo")));
    }

    @Test
    public void buscarServicosPorTitulo() throws Exception {

        List<Servico> servicoList = List.of(servico);
        when(servicoService.listarServicoPorTitulo(Mockito.anyString())).thenReturn(servicoList);
        this.mockMvc.perform(MockMvcRequestBuilders.get(URI+"/titles/Titulo"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(containsString("Titulo")));
    }

    @Test
    public void deletarServicoCaminhoPositivo() throws Exception {

        Mockito.when(servicoService.listarServicoPorId(Mockito.anyLong()))
                .thenReturn(java.util.Optional.ofNullable(servico));

        servicoService.deletarServico(1);

        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.delete(URI+"/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testarDeletarServicoCaminhoNegativo() throws Exception {

        Mockito.doThrow(ServicoNaoEncontradoException.class).when(servicoService).deletarServico(Mockito.anyLong());

        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.delete(URI+"/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }



}
