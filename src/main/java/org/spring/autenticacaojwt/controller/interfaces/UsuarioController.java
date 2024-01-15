package org.spring.autenticacaojwt.controller.interfaces;

import org.spring.autenticacaojwt.dto.SenhaDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

/**
 * Entidade com os métodos padrões do UsuarioController
 *
 */
public interface UsuarioController {
    @PutMapping("{id}/atualizar-senha")
    ResponseEntity<String> atualizarSenha(@PathVariable UUID id, @RequestBody SenhaDTO senhaDTO);
}
