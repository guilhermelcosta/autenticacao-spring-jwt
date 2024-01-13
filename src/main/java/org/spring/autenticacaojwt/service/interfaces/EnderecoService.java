package org.spring.autenticacaojwt.service.interfaces;

import org.jetbrains.annotations.NotNull;
import org.spring.autenticacaojwt.dto.EnderecoDTO;

import java.util.UUID;

/**
 * Entidade com os métodos padrões do EnderecoService
 */
public interface EnderecoService {
    EnderecoDTO encontrarPorId(@NotNull UUID id);
}
