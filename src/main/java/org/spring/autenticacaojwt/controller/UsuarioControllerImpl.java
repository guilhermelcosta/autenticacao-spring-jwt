package org.spring.autenticacaojwt.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.spring.autenticacaojwt.controller.interfaces.OperacoesController;
import org.spring.autenticacaojwt.controller.interfaces.UsuarioController;
import org.spring.autenticacaojwt.dto.SenhaDTO;
import org.spring.autenticacaojwt.dto.UsuarioDTO;
import org.spring.autenticacaojwt.model.Usuario;
import org.spring.autenticacaojwt.service.interfaces.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.spring.autenticacaojwt.util.ConstantesUtil.ENDPOINT_USUARIO;

@Slf4j(topic = "USUARIO_CONTROLLER")
@RestController
@Validated
@RequestMapping(ENDPOINT_USUARIO)
public class UsuarioControllerImpl implements OperacoesController<Usuario, UsuarioDTO>, UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioControllerImpl(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Encontra um usuário a partir do seu id
     *
     * @param id id do usuário
     * @return JSON com o usuário encontrado
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> encontrarPorId(@PathVariable UUID id) {
        log.info(">>> encontrarPorId: recebendo requisição para encontrar usuário por id");
        UsuarioDTO usuario = this.usuarioService.encontrarPorId(id);
        return ResponseEntity.ok().body(usuario);
    }

    /**
     * Lista todos os usuários cadastrados
     *
     * @return lista de usuários cadastrados
     */
    @Override
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarTodos() {
        log.info(">>> listarTodos: recebendo requisição para listar todos usuários");
        List<UsuarioDTO> usuarios = this.usuarioService.listarTodos();
        return ResponseEntity.ok().body(usuarios);
    }

    /**
     * Cria um novo usuário
     *
     * @param usuario objeto do tipo Usuario
     * @return id do novo usuário criado
     */
    @Override
    @PostMapping
    public ResponseEntity<String> criar(@Valid @RequestBody Usuario usuario) {
        log.info(">>> criar: recebendo requisição para criar usuário");
        UsuarioDTO usuarioCriado = this.usuarioService.criar(usuario);
        return ResponseEntity.ok().body(String.format("Usuário criado, id: %s", usuarioCriado.id()));
    }

    /**
     * Atualiza um usuário previamente cadastrado
     *\
     * @param id      id do usuário
     * @param usuario objeto do tipo Usuario (informações atualizadas)
     * @return id do usuário atualizado
     */
    @Override
    @PutMapping("/{id}")
    public ResponseEntity<String> atualizar(@PathVariable UUID id, @Valid @RequestBody @NotNull Usuario usuario) {
        log.info(">>> atualizar: recebendo requisição para atualizar usuário");
        usuario.setId(id);
        UsuarioDTO usuarioAtualizado = this.usuarioService.atualizar(usuario);
        return ResponseEntity.ok().body(String.format("Usuário atualizado, id: %s", usuarioAtualizado.id()));
    }

    /**
     * Deleta um usuário a partir do seu id
     *
     * @param id id do usuário
     * @return id do usuário deletado
     */
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable UUID id) {
        log.info(">>> deletar: recebendo requisição para deletar usuário");
        this.usuarioService.deletar(id);
        return ResponseEntity.ok().body(String.format("Usuário deletado, id: %s", id));
    }

    /**
     * Atualiza a senha de um usuário
     *
     * @param id       id do usuário
     * @param senhaDTO objeto do tipo SenhaDTO
     * @return id do usuário atualizado
     */
    @Override
    @PutMapping("{id}/atualizar-senha")
    public ResponseEntity<String> atualizarSenha(@PathVariable UUID id, @RequestBody SenhaDTO senhaDTO) {
        log.info(">>> atualizarSenha:  recebendo requisição para atualizar senha de usuário");
        this.usuarioService.atualizarSenha(id, senhaDTO);
        return ResponseEntity.ok().body(String.format("Senha atualizada, id do usuário: %s", id));
    }
}
