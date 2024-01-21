package org.spring.sistemaodontologia.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.spring.sistemaodontologia.controller.interfaces.OperacoesCRUDController;
import org.spring.sistemaodontologia.dto.EnderecoDTO;
import org.spring.sistemaodontologia.model.Endereco;
import org.spring.sistemaodontologia.service.interfaces.OperacoesCRUDService;
import org.spring.sistemaodontologia.util.ConversorEntidadeDTOUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.spring.sistemaodontologia.util.ConversorEntidadeDTOUtil.converterParaDTO;
import static org.spring.sistemaodontologia.util.constantes.ConstantesEndpointsUtil.ENDPOINT_ENDERECO;
import static org.spring.sistemaodontologia.util.constantes.ConstantesTopicosUtil.ENDERECO_CONTROLLER;

@Slf4j(topic = ENDERECO_CONTROLLER)
@RestController
@Validated
@RequestMapping(ENDPOINT_ENDERECO)
public class EnderecoControllerImpl implements OperacoesCRUDController<Endereco, EnderecoDTO> {

    private final OperacoesCRUDService<Endereco> operacoesCRUDService;

    public EnderecoControllerImpl(OperacoesCRUDService<Endereco> operacoesCRUDService) {
        this.operacoesCRUDService = operacoesCRUDService;
    }

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
        Endereco endereco = this.operacoesCRUDService.encontrarPorId(id);
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
        List<Endereco> enderecos = this.operacoesCRUDService.listarTodos();
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
    public ResponseEntity<String> criar(@Valid @RequestBody Endereco usuario) {
        log.info(">>> criar: recebendo requisição para criar endereço");
        Endereco enderecoCriado = this.operacoesCRUDService.criar(usuario);
        return ResponseEntity.ok().body(String.format("Endereço criado, id: %s", enderecoCriado.getId()));
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
    public ResponseEntity<String> atualizar(@PathVariable UUID id, @Valid @RequestBody @NotNull Endereco usuario) {
        log.info(">>> atualizar: recebendo requisição para atualizar endereço");
        usuario.setId(id);
        Endereco enderecoAtualizado = this.operacoesCRUDService.atualizar(usuario);
        return ResponseEntity.ok().body(String.format("Endereço atualizado, id: %s", enderecoAtualizado.getId()));
    }

    /**
     * Deleta um endereço a partir do seu id
     *
     * @param id id do endereço
     * @return id do endereço deletado
     */
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable UUID id) {
        log.info(">>> deletar: recebendo requisição para deletar endereço");
        this.operacoesCRUDService.deletar(id);
        return ResponseEntity.ok().body(String.format("Endereço deletado, id: %s", id));
    }

}
