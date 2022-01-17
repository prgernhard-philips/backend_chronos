package com.agenciacronos.siteinstitucional.services;

import com.agenciacronos.siteinstitucional.Repositories.ServicoRepository;
import com.agenciacronos.siteinstitucional.Services.ServicoService;
import com.agenciacronos.siteinstitucional.exceptions.ServicoNaoEncontradoException;
import com.agenciacronos.siteinstitucional.models.Servico;
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
public class ServicoServiceTest {

    @Autowired
    private ServicoService servicoService;

    @MockBean
    private ServicoRepository servicoRepository;

    private Servico servico = new Servico(1, "Titulo","Descricao", "Imagem", LocalDate.now());


    @Test
    public void testarMetodoSalvarServicoCaminhoPositivo(){
        Servico servico = new Servico();
        Mockito.when(servicoRepository.save(Mockito.any(Servico.class)))
                .thenReturn(servico);

        Servico objetoDeTeste = servicoService.salvarServico(servico);
        Assertions.assertEquals(servico, objetoDeTeste);
    }


    @Test
    public void testarMetodoListarTodosOsServicosCaminhoPositivo(){
        Servico servico = new Servico();
        Iterable<Servico> servicoIterable = Arrays.asList(servico);
        Mockito.when(servicoRepository.findAll()).thenReturn((List<Servico>) servicoIterable);

        Assertions.assertTrue(servicoService.listarServicos() instanceof List);
    }

    @Test
    public void testarMetodoListarTodosOsServicosCaminhoPositivoBuscaPeloTitulo(){
        Servico servico = new Servico();
        List<Servico> servicoList= Arrays.asList(servico);
        Mockito.when(servicoRepository.findByTituloContainingIgnoreCase(Mockito.anyString()))
                .thenReturn(servicoList);

        Assertions.assertEquals(servicoList, servicoService.listarServicoPorTitulo("titulo"));
    }

    @Test
    public void testarMetodoListarServicoPorIDCaminhoPositivo(){
        Servico servico = new Servico();
        Optional<Servico> servicoOptional = Optional.of(servico);
        Mockito.when(servicoRepository.findById(Mockito.anyLong())).thenReturn(servicoOptional);

        Assertions.assertEquals(servicoOptional, servicoService.listarServicoPorId(12));
    }

    @Test
    public void testarDeletarServicoPorIDCaminhoPositivo() {
        Mockito.when(servicoRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(servico));
        servicoService.deletarServico(1);
        Mockito.verify(servicoRepository).delete(servico);
    }

    @Test
    public void testrarDeletarServicoPorIDCaminhoNegativo() {
        ServicoNaoEncontradoException servicoNaoEncontradoException = new ServicoNaoEncontradoException(servico.getId());
        Mockito.when(servicoRepository.findById(Mockito.anyLong()))
                .thenThrow(servicoNaoEncontradoException);
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            servicoService.deletarServico(10);
        });
        Assertions.assertNotNull(exception);

    }



}
