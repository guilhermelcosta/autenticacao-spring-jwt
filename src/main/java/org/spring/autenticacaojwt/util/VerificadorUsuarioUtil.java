package org.spring.autenticacaojwt.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.spring.autenticacaojwt.excecoes.lancaveis.UsuarioNaoAutorizadoException;
import org.spring.autenticacaojwt.seguranca.UsuarioDetails;
import org.spring.autenticacaojwt.util.enums.PerfilUsuario;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

import static java.util.Objects.isNull;

@UtilityClass
@Slf4j(topic = "VERIFICADOR_USUARIO")
public class VerificadorUsuarioUtil {

    /**
     * Verifica as autorizações de um usuário
     * Essa versão do método suporta que uma requisição seja realizada por usuários não que não possuam perfil ADMIN
     *
     * @param idUsuarioCorrespondente id do objeto do tipo Usuario relacionado à requisição
     * @return usuário autorizado
     */
    public static UsuarioDetails verificarAutorizacao(UUID idUsuarioCorrespondente) {

        log.info(">>> verificarAutorizacao: verificando autorizações do usuário ativo");
        UsuarioDetails usuarioDetails = autenticar();

        if (isNull(usuarioDetails))
            throw new UsuarioNaoAutorizadoException("usuário não logado");

        if (!(usuarioEhAdmin(usuarioDetails) || idUsuarioCorrespondente.equals(usuarioDetails.getId())))
            throw new UsuarioNaoAutorizadoException("usuário não possui autorização para utilizar esse método");

        log.info(String.format(">>> verificarAutorizacao: usuário [%s] autorizado para realizar requisição", usuarioDetails.getUsername()));
        return usuarioDetails;
    }

    /**
     * Verifica as autorizações de um usuário
     * Essa versão do método bloqueia qualquer requisição por usuários não ADMINS
     *
     * @return usuário autorizado
     */
    public static UsuarioDetails verificarAutorizacao() {

        log.info(">>> verificarAutorizacao: verificando autorizações do usuário ativo");
        UsuarioDetails usuarioDetails = autenticar();

        if (isNull(usuarioDetails))
            throw new UsuarioNaoAutorizadoException("usuário não logado");

        if (!usuarioEhAdmin(usuarioDetails))
            throw new UsuarioNaoAutorizadoException("usuário não possui autorização para utilizar esse método");

        log.info(String.format(">>> verificarAutorizacao: usuário [%s] autorizado para realizar requisição", usuarioDetails.getUsername()));
        return usuarioDetails;
    }

    /**
     * Autentica usuário
     *
     * @return usuário autenticado, caso contrário null
     */
    private static UsuarioDetails autenticar() {
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
    private static boolean usuarioEhAdmin(UsuarioDetails userSpringSecurity) {
        return userSpringSecurity.ehPerfil(PerfilUsuario.ADMIN);
    }
}
