package org.spring.autenticacaojwt.excecoes.lancaveis;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DeletarEntidadeException extends RuntimeException {

    public DeletarEntidadeException(String mensagem) {
        super(mensagem);
    }
}
