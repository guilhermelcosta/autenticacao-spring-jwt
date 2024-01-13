package org.spring.autenticacaojwt.service.interfaces;

import org.jetbrains.annotations.NotNull;
import org.spring.autenticacaojwt.dto.SenhaDTO;
import org.spring.autenticacaojwt.dto.UsuarioDTO;
import org.spring.autenticacaojwt.model.Usuario;

import java.util.List;
import java.util.UUID;

/**
 * Entidade com os métodos padrões do UsuarioService
 */
public interface UsuarioService {

    UsuarioDTO encontrarPorId(@NotNull UUID id);

    List<UsuarioDTO> listarTodos();

    UsuarioDTO criar(@NotNull Usuario usuario);

    UsuarioDTO atualizar(@NotNull Usuario usuario);

    void deletar(@NotNull UUID id);

    void atualizarSenha(@NotNull UUID id, @NotNull SenhaDTO senhaDTO);

    Usuario encontrarUsuarioPorEmail(@NotNull String email);
}
