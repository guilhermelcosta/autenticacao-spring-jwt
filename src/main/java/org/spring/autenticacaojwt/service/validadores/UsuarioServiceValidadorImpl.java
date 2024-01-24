package org.spring.autenticacaojwt.service.validadores;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.spring.autenticacaojwt.seguranca.UsuarioDetails;
import org.spring.autenticacaojwt.service.interfaces.Validador;

import java.util.UUID;

import static org.spring.autenticacaojwt.util.constantes.ConstantesTopicosUtil.USUARIO_SERVICE;

@AllArgsConstructor
public class UsuarioServiceValidadorImpl implements Validador {

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
        return USUARIO_SERVICE;
    }
}
