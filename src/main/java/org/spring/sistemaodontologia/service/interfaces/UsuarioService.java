package org.spring.sistemaodontologia.service.interfaces;

import org.jetbrains.annotations.NotNull;
import org.spring.sistemaodontologia.dto.SenhaDTO;
import org.spring.sistemaodontologia.model.Usuario;

import java.util.UUID;

/**
 * Entidade com os métodos específicos do UsuarioService
 */
public interface UsuarioService {

    Usuario encontrarPorEmail(@NotNull String email);
    void atualizarSenha(@NotNull UUID id, @NotNull SenhaDTO senhaDTO);
}
