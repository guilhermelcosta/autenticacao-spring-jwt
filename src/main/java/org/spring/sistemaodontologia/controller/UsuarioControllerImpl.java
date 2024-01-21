package org.spring.sistemaodontologia.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.spring.sistemaodontologia.controller.interfaces.OperacoesCRUDController;
import org.spring.sistemaodontologia.controller.interfaces.UsuarioController;
import org.spring.sistemaodontologia.dto.SenhaDTO;
import org.spring.sistemaodontologia.dto.UsuarioDTO;
import org.spring.sistemaodontologia.model.Usuario;
import org.spring.sistemaodontologia.service.interfaces.OperacoesCRUDService;
import org.spring.sistemaodontologia.service.interfaces.UsuarioService;
import org.spring.sistemaodontologia.util.ConversorEntidadeDTOUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.spring.sistemaodontologia.util.ConversorEntidadeDTOUtil.converterParaDTO;
import static org.spring.sistemaodontologia.util.constantes.ConstantesEndpointsUtil.ENDPOINT_USUARIO;
import static org.spring.sistemaodontologia.util.constantes.ConstantesTopicosUtil.USUARIO_CONTROLLER;

@Slf4j(topic = USUARIO_CONTROLLER)
@RestController
@Validated
@RequestMapping(ENDPOINT_USUARIO)
public class UsuarioControllerImpl implements OperacoesCRUDController<Usuario, UsuarioDTO>, UsuarioController {

    private final OperacoesCRUDService<Usuario> operacoesCRUDService;
    private final UsuarioService usuarioService;

    public UsuarioControllerImpl(UsuarioService usuarioService, OperacoesCRUDService<Usuario> operacoesCRUDService) {
        this.usuarioService = usuarioService;
        this.operacoesCRUDService = operacoesCRUDService;
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
        Usuario usuario = this.operacoesCRUDService.encontrarPorId(id);
        return ResponseEntity.ok().body(converterParaDTO(usuario));
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
        List<Usuario> usuarios = this.operacoesCRUDService.listarTodos();
        return ResponseEntity.ok().body(usuarios.stream().map(ConversorEntidadeDTOUtil::converterParaDTO).toList());
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
        Usuario usuarioCriado = this.operacoesCRUDService.criar(usuario);
        return ResponseEntity.ok().body(String.format("Usuário criado, id: %s", usuarioCriado.getId()));
    }

    /**
     * Atualiza um usuário previamente cadastrado
     *
     * @param id      id do usuário
     * @param usuario objeto do tipo Usuario (informações atualizadas)
     * @return id do usuário atualizado
     */
    @Override
    @PutMapping("/{id}")
    public ResponseEntity<String> atualizar(@PathVariable UUID id, @Valid @RequestBody @NotNull Usuario usuario) {
        log.info(">>> atualizar: recebendo requisição para atualizar usuário");
        usuario.setId(id);
        Usuario usuarioAtualizado = this.operacoesCRUDService.atualizar(usuario);
        return ResponseEntity.ok().body(String.format("Usuário atualizado, id: %s", usuarioAtualizado.getId()));
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
        this.operacoesCRUDService.deletar(id);
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
