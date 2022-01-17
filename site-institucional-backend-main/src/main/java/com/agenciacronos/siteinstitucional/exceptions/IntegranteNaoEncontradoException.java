package com.agenciacronos.siteinstitucional.exceptions;

import com.agenciacronos.siteinstitucional.SiteInstitucionalApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IntegranteNaoEncontradoException extends RuntimeException{

    private static Logger logger = LoggerFactory.getLogger(SiteInstitucionalApplication.class);

    public IntegranteNaoEncontradoException(long id){
        super("Integrante não encontrado com o id: "+ id);
        logger.info("Integrante com id: "+id+" não encontrado");
    }
}
