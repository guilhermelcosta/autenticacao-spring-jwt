package org.spring.autenticacaojwt.service;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spring.autenticacaojwt.enums.PerfilUsuario;
import org.spring.autenticacaojwt.excecao.lancaveis.TopicoNaoEncontradoException;
import org.spring.autenticacaojwt.excecao.lancaveis.UsuarioNaoAutorizadoException;
import org.spring.autenticacaojwt.repository.EnderecoRepository;
import org.spring.autenticacaojwt.seguranca.UsuarioDetails;
import org.spring.autenticacaojwt.service.interfaces.ValidadorAutorizacaoRequisicaoService;
import org.spring.autenticacaojwt.validadores.EnderecoServiceValidador;
import org.spring.autenticacaojwt.validadores.UsuarioServiceValidador;
import org.spring.autenticacaojwt.validadores.Validador;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;
import static org.spring.autenticacaojwt.util.constantes.ConstantesTopicosUtil.VALIDADOR_AUTORIZACAO_REQUISICAO_SERVICE;

@Slf4j(topic = VALIDADOR_AUTORIZACAO_REQUISICAO_SERVICE)
@Service
public class ValidadorAutorizacaoRequisicaoServiceImpl implements ValidadorAutorizacaoRequisicaoService {

    private final List<Validador> validadores;

    public ValidadorAutorizacaoRequisicaoServiceImpl(EnderecoRepository enderecoRepository) {
        this.validadores = new ArrayList<>(Arrays.asList(
                new UsuarioServiceValidador(),
                new EnderecoServiceValidador(enderecoRepository)));
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

        UsuarioDetails usuarioDetails = validarUsuarioLogado();

        boolean usuarioAutorizado = validadores.stream().
                filter(validador -> validador.getTopico().equals(topico))
                .findFirst()
                .orElseThrow(() -> new TopicoNaoEncontradoException(String.format("tópico não encontrado: %s", topico)))
                .validar(id, usuarioDetails);

        if (!(usuarioEhAdmin(usuarioDetails) || usuarioAutorizado))
            throw new UsuarioNaoAutorizadoException(String.format("usuário [%s] não possui autorização para utilizar esse método", usuarioDetails.getUsername()));

        log.info(String.format(">>> validarAutorizacaoRequisicao: usuário [%s] autorizado para realizar requisição", usuarioDetails.getUsername()));
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

        UsuarioDetails usuarioDetails = validarUsuarioLogado();

        if (!usuarioEhAdmin(usuarioDetails))
            throw new UsuarioNaoAutorizadoException(String.format("usuário [%s] não possui autorização para utilizar esse método", usuarioDetails.getUsername()));

        log.info(String.format(">>> validarAutorizacaoRequisicao: usuário [%s] autorizado para realizar requisição", usuarioDetails.getUsername()));
        return usuarioDetails;
    }

    /**
     * Valida se um usuário está logado
     *
     * @return objeto do tipo UsuarioDetails, caso o usuário esteja logado
     */
    private UsuarioDetails validarUsuarioLogado() {

        UsuarioDetails usuarioDetails = autenticar();

        if (isNull(usuarioDetails))
            throw new UsuarioNaoAutorizadoException("usuário não logado");

        return usuarioDetails;
    }

    /**
     * Autentica usuário
     *
     * @return usuário autenticado, caso contrário null
     */
    private @Nullable UsuarioDetails autenticar() {
        try {
            return (UsuarioDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            return null;
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
