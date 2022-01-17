package com.agenciacronos.siteinstitucional.exceptions;

public class ServicoNaoEncontradoException  extends RuntimeException{

    public ServicoNaoEncontradoException(long id) {
        super("Serviço não encontrado com o id: "+ id);
    }
}
