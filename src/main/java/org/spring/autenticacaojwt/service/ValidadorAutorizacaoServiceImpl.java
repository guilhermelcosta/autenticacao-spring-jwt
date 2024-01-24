package org.spring.autenticacaojwt.service;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spring.autenticacaojwt.enums.PerfilUsuario;
import org.spring.autenticacaojwt.excecao.lancaveis.TopicoNaoEncontradoException;
import org.spring.autenticacaojwt.excecao.lancaveis.UsuarioNaoAutorizadoException;
import org.spring.autenticacaojwt.repository.EnderecoRepository;
import org.spring.autenticacaojwt.seguranca.UsuarioDetails;
import org.spring.autenticacaojwt.service.interfaces.Validador;
import org.spring.autenticacaojwt.service.interfaces.ValidadorAutorizacaoRequisicaoService;
import org.spring.autenticacaojwt.service.validadores.EnderecoServiceValidadorImpl;
import org.spring.autenticacaojwt.service.validadores.UsuarioServiceValidadorImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static org.spring.autenticacaojwt.util.constantes.ConstantesTopicosUtil.VALIDADOR_AUTORIZACAO_REQUISICAO_SERVICE;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Slf4j(topic = VALIDADOR_AUTORIZACAO_REQUISICAO_SERVICE)
@Service
public class ValidadorAutorizacaoServiceImpl implements ValidadorAutorizacaoRequisicaoService {

    private final List<Validador> validadores;

    private ValidadorAutorizacaoServiceImpl(EnderecoRepository enderecoRepository) {
        this.validadores = new ArrayList<>(asList(
                new UsuarioServiceValidadorImpl(),
                new EnderecoServiceValidadorImpl(enderecoRepository)));
    }

    /**
     * Verifica as autorizações de um usuário
     * Essa versão do método suporta que uma requisição seja realizada por usuários não que não possuam perfil ADMIN
     *
     * @param id id do objeto do tipo Usuario relacionado à requisição
     * @return usuário autorizado
     */
    @Override
    public UsuarioDetails validarAutorizacaoRequisicao(UUID id, String topico) {

        UsuarioDetails usuarioDetails = autenticar();

        boolean usuarioAutorizado = validadores.stream().
                filter(validador -> validador.getTopico().equals(topico))
                .findFirst()
                .orElseThrow(() -> new TopicoNaoEncontradoException(format("tópico não encontrado: %s", topico)))
                .validar(id, usuarioDetails);

        if (!(usuarioEhAdmin(requireNonNull(usuarioDetails)) || usuarioAutorizado))
            throw new UsuarioNaoAutorizadoException(format("usuário [%s] não possui autorização para utilizar esse método", usuarioDetails.getUsername()));

        log.info(format(">>> validarAutorizacaoRequisicao: usuário [%s] autorizado para realizar requisição", usuarioDetails.getUsername()));
        return usuarioDetails;
    }

    /**
     * Verifica as autorizações de um usuário
     * Essa versão do método bloqueia qualquer requisição por usuários não ADMINS
     *
     * @return usuário autorizado
     */
    @Override
    public UsuarioDetails validarAutorizacaoRequisicao() {

        UsuarioDetails usuarioDetails = autenticar();

        if (!usuarioEhAdmin(requireNonNull(usuarioDetails)))
            throw new UsuarioNaoAutorizadoException(format("usuário [%s] não possui autorização para utilizar esse método", usuarioDetails.getUsername()));

        log.info(format(">>> validarAutorizacaoRequisicao: usuário [%s] autorizado para realizar requisição", usuarioDetails.getUsername()));
        return usuarioDetails;
    }

    /**
     * Autentica usuário
     *
     * @return usuário autenticado, caso contrário null
     */
    private @Nullable UsuarioDetails autenticar() {
        try {
            return (UsuarioDetails) getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new UsuarioNaoAutorizadoException("usuário não logado");
        }
    }

    /**
     * Verifica se um usuário é administrador
     *
     * @param userSpringSecurity usuário
     * @return boolean indicando se usuário é administrador ou não
     */
    private boolean usuarioEhAdmin(@NotNull UsuarioDetails userSpringSecurity) {
        return userSpringSecurity.ehPerfil(PerfilUsuario.ADMIN);
    }
}
