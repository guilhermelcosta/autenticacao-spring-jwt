package org.spring.sistemaodontologia.validadores;

import org.spring.sistemaodontologia.seguranca.UsuarioDetails;

import java.util.UUID;

/**
 * Realiza as validações para ver se um usuário pode realizar uma determinada requisição
 */
public interface Validador {

    boolean validar(UUID id, UsuarioDetails usuarioDetails);

    String getTopico();
}
