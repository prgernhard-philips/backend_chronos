package com.agenciacronos.siteinstitucional.controller;

import com.agenciacronos.siteinstitucional.Services.IntegranteService;
import com.agenciacronos.siteinstitucional.exceptions.IntegranteNaoEncontradoException;
import com.agenciacronos.siteinstitucional.models.Integrante;
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
@WebMvcTest(IntegranteController.class)
public class IntegranteControllerTest {

    @MockBean
    private IntegranteService integranteService;

    //MockMvc é a classe responsavel por simular requisoções HTTP no endpoint do controller
    @Autowired
    private MockMvc mockMvc;

    private Integrante integrante;
    private ObjectMapper objectMapper;
    private List<Integrante> integrantes;

    private String URI = "/v1/integrants";

    @BeforeEach
    void setUp(){
        integrantes = List.of(new Integrante(1, "Teste", "testeImagem", "Teste", "teste","teste", LocalDate.now()));

        integrante = new Integrante("Teste", "testeImagem", "Teste", "teste","teste");

        objectMapper = new ObjectMapper();
    }


    @Test
    void cadastrarIntegrantesTest() throws Exception {

        when(integranteService.salvarIntegrante(Mockito.any(Integrante.class))).thenReturn(integrante);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(integrante).getBytes(StandardCharsets.UTF_8))
                        .accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        Assertions.assertThat(result).isNotNull();
        String integranteJson = result.getResponse().getContentAsString();
        Assertions.assertThat(integranteJson).isNotEmpty();
        Assertions.assertThat(integranteJson).isEqualToIgnoringCase(objectMapper.writeValueAsString(integrante));
    }


    @Test
    public void testarBuscaDeIntegrantePorId() throws Exception{
        when(integranteService.listarIntegrantePorId(Mockito.anyLong()))
                .thenReturn(java.util.Optional.ofNullable(integrante));

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(URI+"/42")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk());

        String jsonDeResposta = resultActions.andReturn().getResponse().getContentAsString();
        Integrante integrante = objectMapper.readValue(jsonDeResposta, Integrante.class);

    }

    @Test
    public void testarBuScaDeIntegrantePeloIdNaoExiste() throws Exception {
        when(integranteService.listarIntegrantePorId(Mockito.anyInt()))
                .thenThrow(RuntimeException.class);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(URI+"/60")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void buscarTodosIntegrantes() throws Exception {

        List<Integrante> integranteList = List.of(integrante);
        when(integranteService.listarIntegrantes()).thenReturn(integranteList);
        this.mockMvc.perform(MockMvcRequestBuilders.get(URI))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(containsString("Teste")));
    }

    @Test
    public void buscarIntegrantesPorNome() throws Exception {

        List<Integrante> integranteList = List.of(integrante);
        when(integranteService.listarIntegrantePorNome(Mockito.anyString())).thenReturn(integranteList);
        this.mockMvc.perform(MockMvcRequestBuilders.get(URI+"/names/Teste"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(containsString("Teste")));
    }

    @Test
    public void deletarIntegranteCaminhoPositivo() throws Exception {

        Mockito.when(integranteService.listarIntegrantePorId(Mockito.anyLong()))
                .thenReturn(java.util.Optional.ofNullable(integrante));

        integranteService.deletarIntegrante(1);

        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.delete(URI+"/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testarDeletarIntegranteCaminhoNegativo() throws Exception {

        Mockito.doThrow(IntegranteNaoEncontradoException.class).when(integranteService).deletarIntegrante(Mockito.anyLong());

        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.delete(URI+"/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


}
