package org.spring.autenticacaojwt.controller.interfaces;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Entidade com os métodos padrões do controller
 *
 * @param <I> Classe do controller
 */
public interface OperacoesController<I, O> {

    @GetMapping("/{id}")
    ResponseEntity<O> encontrarPorId(@PathVariable UUID id);

    @GetMapping
    ResponseEntity<List<O>> listarTodos();

    @PostMapping
    ResponseEntity<String> criar(@Valid @RequestBody I obj);

    @PutMapping("/{id}")
    ResponseEntity<String> atualizar(@PathVariable UUID id, @Valid @RequestBody I obj);

    @DeleteMapping("/{id}")
    ResponseEntity<String> deletar(@PathVariable UUID id);
}
