package org.spring.autenticacaojwt.service.interfaces;

import org.spring.autenticacaojwt.seguranca.UsuarioDetails;

import java.util.UUID;

/**
 * Valida a autorização do usuário logado para realizar uma determinada requisição
 */
public interface ValidadorAutorizacaoRequisicaoService {

    UsuarioDetails validarAutorizacaoRequisicao(UUID id, String topico);

    UsuarioDetails validarAutorizacaoRequisicao();
}
