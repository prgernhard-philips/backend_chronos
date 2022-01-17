package com.agenciacronos.siteinstitucional.services;

import com.agenciacronos.siteinstitucional.Repositories.IntegranteRepository;
import com.agenciacronos.siteinstitucional.Services.IntegranteService;
import com.agenciacronos.siteinstitucional.exceptions.IntegranteNaoEncontradoException;
import com.agenciacronos.siteinstitucional.models.Integrante;
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
public class IntegranteServiceTest {

    @Autowired
    private IntegranteService integranteService;
    @MockBean
    private IntegranteRepository integranteRepository;

    private Integrante integrante = new Integrante(1, "fulano", "imagem", "funcao", "linkedin","github",LocalDate.now());


    @Test
    public void testarMetodoSalvarIntegranteCaminhoPositivo(){
        Integrante integrante = new Integrante();
        Mockito.when(integranteRepository.save(Mockito.any(Integrante.class)))
                .thenReturn(integrante);

        Integrante objetoDeTeste = integranteService.salvarIntegrante(integrante);
        Assertions.assertEquals(integrante, objetoDeTeste);

    }


    @Test
    public void testarMetodoListarTodosOsIntegrantesCaminhoPositivo(){
        Integrante integrante = new Integrante();
        Iterable<Integrante> integranteIterable = Arrays.asList(integrante);
        Mockito.when(integranteRepository.findAll()).thenReturn((List<Integrante>) integranteIterable);

        Assertions.assertTrue(integranteService.listarIntegrantes() instanceof List);
    }

    @Test
    public void testarMetodoListarTodosOsIntegrantesCaminhoPositivoBuscaPeloNome(){
        Integrante integrante = new Integrante();
        List<Integrante> integranteList= Arrays.asList(integrante);
        Mockito.when(integranteRepository.findByNomeContainingIgnoreCase(Mockito.anyString()))
                .thenReturn(integranteList);

        Assertions.assertEquals(integranteList, integranteService.listarIntegrantePorNome("qualquercoisa"));
    }

    @Test
    public void testarMetodoListarIntegrantePorIDCaminhoPositivo(){
        Integrante integrante = new Integrante();
        Optional<Integrante> integranteOptional = Optional.of(integrante);
        Mockito.when(integranteRepository.findById(Mockito.anyLong())).thenReturn(integranteOptional);

        Assertions.assertEquals(integranteOptional, integranteService.listarIntegrantePorId(12));
    }

    @Test
    public void testarDeletarIntegrantePorIDCaminhoPositivo() {
        Mockito.when(integranteRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(integrante));
        integranteService.deletarIntegrante(1);
        Mockito.verify(integranteRepository).delete(integrante);
    }

    @Test
    public void testrarDeletarIntegrantePorIDCaminhoNegativo() {
        IntegranteNaoEncontradoException integranteNaoEncontradoException = new IntegranteNaoEncontradoException(integrante.getId());
        Mockito.when(integranteRepository.findById(Mockito.anyLong()))
                .thenThrow(integranteNaoEncontradoException);
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            integranteService.deletarIntegrante(10);
        });
        Assertions.assertNotNull(exception);

    }





}
