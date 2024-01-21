package org.spring.sistemaodontologia.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.spring.sistemaodontologia.enums.UnidadeFederativa;

import java.util.UUID;

@Builder
public record EnderecoDTO(
        UUID id,
        @JsonProperty("id_usuario") UUID idUsuario,
        String rua,
        String numero,
        String bairro,
        String complemento,
        String cep,
        String cidade,
        UnidadeFederativa estado) {
}
