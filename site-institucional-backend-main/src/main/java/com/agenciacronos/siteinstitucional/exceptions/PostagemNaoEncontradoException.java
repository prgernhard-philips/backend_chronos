package com.agenciacronos.siteinstitucional.exceptions;

public class PostagemNaoEncontradoException extends RuntimeException{

    public PostagemNaoEncontradoException(long id) {
        super("Postagem não encontrado com o id: "+ id);
    }
}
