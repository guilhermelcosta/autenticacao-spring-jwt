package org.spring.autenticacaojwt.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.spring.autenticacaojwt.controller.UsuarioControllerImpl;
import org.spring.autenticacaojwt.dto.EnderecoDTO;
import org.spring.autenticacaojwt.dto.UsuarioDTO;
import org.spring.autenticacaojwt.model.Endereco;
import org.spring.autenticacaojwt.model.Usuario;

import java.util.Collections;

import static org.spring.autenticacaojwt.util.constantes.ConstantesTopicosUtil.CONVERSOR_ENTIDADE_DTO_UTIL;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@UtilityClass
@Slf4j(topic = CONVERSOR_ENTIDADE_DTO_UTIL)
public class ConversorEntidadeDTOUtil {

    /**
     * Converte uma entidade do tipo Usuario para UsuarioDTO
     *
     * @param usuario entidade do tipo Usuario
     * @return novo UsuarioDTO
     */
    public static UsuarioDTO converterParaDTO(Usuario usuario) {
        log.info(String.format(">>> converterParaDTO: convertendo Usuario (id: %s) para DTO", usuario.getId()));
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .idEndereco(usuario.getEndereco().getId())
                .perfilUsuario(usuario.getPerfilUsuario())
                .nome(usuario.getNome())
                .rg(usuario.getRg())
                .cpf(usuario.getCpf())
                .dataNascimento(usuario.getDataNascimento())
                .email(usuario.getEmail())
                .telefone(usuario.getTelefone())
                .dataCriacao(usuario.getDataCriacao())
                .dataUltimaModificacao(usuario.getDataUltimaModificacao())
                .links(Collections.singletonList(linkTo(UsuarioControllerImpl.class).slash(usuario.getId()).withSelfRel()))
                .build();
    }

    /**
     * Converte uma entidade do tipo Endereco para EnderecoDTO
     *
     * @param endereco entidade do tipo Endereco
     * @return novo EnderecoDTO
     */
    public static EnderecoDTO converterParaDTO(Endereco endereco) {
        log.info(String.format(">>> converterParaDTO: convertendo Endereco (id: %s) para DTO", endereco.getId()));
        return EnderecoDTO.builder()
                .id(endereco.getId())
                .idUsuario(endereco.getUsuario() != null ? endereco.getUsuario().getId() : null)
                .rua(endereco.getRua())
                .numero(endereco.getNumero())
                .bairro(endereco.getBairro())
                .complemento(endereco.getComplemento())
                .cep(endereco.getCep())
                .cidade(endereco.getCidade())
                .estado(endereco.getEstado())
                .build();
    }
}
