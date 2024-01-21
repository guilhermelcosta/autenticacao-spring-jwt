package org.spring.autenticacaojwt.service.interfaces;

import org.jetbrains.annotations.NotNull;
import org.spring.autenticacaojwt.dto.SenhaDTO;
import org.spring.autenticacaojwt.model.Usuario;

import java.util.UUID;

/**
 * Entidade com os métodos específicos do UsuarioService
 */
public interface UsuarioService {

    Usuario encontrarPorEmail(@NotNull String email);
    void atualizarSenha(@NotNull UUID id, @NotNull SenhaDTO senhaDTO);
}
