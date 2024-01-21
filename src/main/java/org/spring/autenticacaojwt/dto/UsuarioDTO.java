package org.spring.autenticacaojwt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.hateoas.Link;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record UsuarioDTO(
        UUID id,
        @JsonProperty("id_endereco") UUID idEndereco,
        @JsonProperty("perfil_usuario") Integer perfilUsuario,
        String nome,
        String rg,
        String cpf,
        @JsonProperty("data_nascimento") LocalDate dataNascimento,
        String email,
        String telefone,
        @JsonProperty("data_criacao") LocalDateTime dataCriacao,
        @JsonProperty("data_ultima_modificacao") LocalDateTime dataUltimaModificacao,
        List<Link> links) {
}
