package org.spring.sistemaodontologia.excecao.lancaveis;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UsuarioNaoAutorizadoException extends RuntimeException {

    public UsuarioNaoAutorizadoException(String mensagem) {
        super(mensagem);
    }
}
