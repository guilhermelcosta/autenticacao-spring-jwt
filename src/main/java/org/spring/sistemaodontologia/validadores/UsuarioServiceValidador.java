package org.spring.sistemaodontologia.validadores;

import org.jetbrains.annotations.NotNull;
import org.spring.sistemaodontologia.seguranca.UsuarioDetails;

import java.util.UUID;

import static org.spring.sistemaodontologia.util.constantes.ConstantesTopicosUtil.USUARIO_SERVICE;


public class UsuarioServiceValidador implements Validador {

    private static final String TOPICO_VALIDADOR = USUARIO_SERVICE;

    /**
     * Valida um usuário para efetuar uma requisição
     *
     * @param idUsuario     id do usuário relativo à requisição
     * @param usuarioDetails objeto do tipo usuarioDetails
     * @return boolean indicando se o usuário foi ou não validado
     */
    @Override
    public boolean validar(UUID idUsuario, @NotNull UsuarioDetails usuarioDetails) {
        return usuarioDetails.getId().equals(idUsuario);
    }

    /**
     * Obtém o tópico do validador
     *
     * @return tópico
     */
    @Override
    public String getTopico() {
        return TOPICO_VALIDADOR;
    }
}
