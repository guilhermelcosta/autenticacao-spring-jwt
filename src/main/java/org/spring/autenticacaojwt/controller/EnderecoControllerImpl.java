package org.spring.autenticacaojwt.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.spring.autenticacaojwt.controller.interfaces.OperacoesCRUDController;
import org.spring.autenticacaojwt.dto.EnderecoDTO;
import org.spring.autenticacaojwt.model.Endereco;
import org.spring.autenticacaojwt.service.interfaces.OperacoesCRUDService;
import org.spring.autenticacaojwt.util.ConversorEntidadeDTOUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.spring.autenticacaojwt.util.ConstrutorRespostaJsonUtil.construirRespostaJSON;
import static org.spring.autenticacaojwt.util.ConversorEntidadeDTOUtil.converterParaDTO;
import static org.spring.autenticacaojwt.util.constantes.ConstantesRequisicaoUtil.*;
import static org.spring.autenticacaojwt.util.constantes.ConstantesTopicosUtil.ENDERECO_CONTROLLER;
import static org.springframework.http.HttpStatus.OK;

@Slf4j(topic = ENDERECO_CONTROLLER)
@RestController
@Validated
@RequestMapping(ENDPOINT_ENDERECO)
@AllArgsConstructor
public class EnderecoControllerImpl implements OperacoesCRUDController<Endereco, EnderecoDTO> {

    private final OperacoesCRUDService<Endereco> operacoesCRUDService;

    /**
     * Encontra um endereço a partir do seu id
     *
     * @param id id do endereço
     * @return JSON com o endereço encontrado
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<EnderecoDTO> encontrarPorId(@PathVariable UUID id) {
        log.info(">>> encontrarPorId: recebendo requisição para encontrar endereço por id");
        Endereco endereco = operacoesCRUDService.encontrarPorId(id);
        return ResponseEntity.ok().body(converterParaDTO(endereco));
    }

    /**
     * Lista todos os endereços cadastrados
     *
     * @return lista de endereços cadastrados
     */
    @Override
    @GetMapping
    public ResponseEntity<List<EnderecoDTO>> listarTodos() {
        log.info(">>> listarTodos: recebendo requisição para listar todos endereços");
        List<Endereco> enderecos = operacoesCRUDService.listarTodos();
        return ResponseEntity.ok().body(enderecos.stream().map(ConversorEntidadeDTOUtil::converterParaDTO).toList());
    }

    /**
     * Cria um novo endereço
     *
     * @param usuario objeto do tipo Endereco
     * @return id do novo endereço criado
     */
    @Override
    @PostMapping
    public ResponseEntity<Map<String, Object>> criar(@Valid @RequestBody Endereco usuario) {
        log.info(">>> criar: recebendo requisição para criar endereço");
        Endereco enderecoCriado = operacoesCRUDService.criar(usuario);
        return ResponseEntity.ok().body(construirRespostaJSON(CHAVES_ENDERECO_CONTROLLER, asList(OK.value(), MSG_ENDERECO_CRIADO, enderecoCriado.getId())));
    }

    /**
     * Atualiza um endereço previamente cadastrado
     *
     * @param id      id do endereço
     * @param usuario objeto do tipo Endereco (informações atualizadas)
     * @return id do endereço atualizado
     */
    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> atualizar(@PathVariable UUID id, @Valid @RequestBody @NotNull Endereco usuario) {
        log.info(">>> atualizar: recebendo requisição para atualizar endereço");
        usuario.setId(id);
        Endereco enderecoAtualizado = operacoesCRUDService.atualizar(usuario);
        return ResponseEntity.ok().body(construirRespostaJSON(CHAVES_ENDERECO_CONTROLLER, asList(OK.value(), MSG_ENDERECO_ATUALIZADO, enderecoAtualizado.getId())));
    }

    /**
     * Deleta um endereço a partir do seu id
     *
     * @param id id do endereço
     * @return id do endereço deletado
     */
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletar(@PathVariable UUID id) {
        log.info(">>> deletar: recebendo requisição para deletar endereço");
        operacoesCRUDService.deletar(id);
        return ResponseEntity.ok().body(construirRespostaJSON(CHAVES_ENDERECO_CONTROLLER, asList(OK.value(), MSG_ENDERECO_DELETADO, id)));

    }
}
