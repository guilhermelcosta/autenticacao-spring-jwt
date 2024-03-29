package org.spring.autenticacaojwt.seguranca;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.spring.autenticacaojwt.componentes.JWTComp;
import org.spring.autenticacaojwt.excecao.InterceptadorExcecoes;
import org.spring.autenticacaojwt.model.Usuario;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static java.lang.String.format;
import static org.spring.autenticacaojwt.util.constantes.ConstantesRequisicaoUtil.*;
import static org.spring.autenticacaojwt.util.constantes.ConstantesTopicosUtil.JWT_FILTRO_AUTENTICACAO;

@Slf4j(topic = JWT_FILTRO_AUTENTICACAO)
public class JWTFiltroAutenticacao extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTComp jwtComp;

    public JWTFiltroAutenticacao(AuthenticationManager authenticationManager, JWTComp jwtComp) {
        setAuthenticationFailureHandler(new InterceptadorExcecoes());
        this.authenticationManager = authenticationManager;
        this.jwtComp = jwtComp;
    }


    /**
     * Tenta realizar a autenticação do usuário pelo endpoint /login
     *
     * @param request  requisição
     * @param response resposta
     * @return autenticação
     * @throws AuthenticationException lança exceção caso haja erro durante a autenticação do usuário
     */
    @Override
    public Authentication attemptAuthentication(@NotNull HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        Usuario usuario;
        try {
            usuario = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
        } catch (IOException e) {
            throw new RuntimeException(format("[ERRO] IOException: falha ao ler autenticação do usuário: %s", e));
        }
        log.info("=========================================================================");
        log.info(format(">>> attemptAuthentication: realizando autenticação do usuário: %s", usuario.getEmail()));
        UsernamePasswordAuthenticationToken tokenAuth = new UsernamePasswordAuthenticationToken(usuario.getEmail(), usuario.getSenha(), new ArrayList<>());
        return this.authenticationManager.authenticate(tokenAuth);
    }

    /**
     * Formata os headers de resposta caso a autenticação seja realizada com sucesso
     *
     * @param request        requisição
     * @param response       resposta
     * @param filterChain    camada de segurança Filter Chain
     * @param authentication autenticação do usuário
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, @NotNull HttpServletResponse response, FilterChain filterChain, @NotNull Authentication authentication) {
        log.info(">>> successfulAuthentication: autenticação realizada com sucesso");
        UsuarioDetails usuarioDetails = (UsuarioDetails) authentication.getPrincipal();
        String emailUsuario = usuarioDetails.getUsername();
        String token = this.jwtComp.gerarToken(emailUsuario);
        response.addHeader(HEADER_AUTORIZACAO, format(VALOR_HEADER_AUTORIZACAO, token));
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(CHARACTER_ENCODING);
        try {
            response.getWriter().write(format(CORPO_RESPOSTA_REQUISICAO, token, emailUsuario, LocalDateTime.now()));
        } catch (IOException e) {
            throw new RuntimeException(format("[ERRO] IOException: falha ao escrever headers de resposta: %s", e));
        }
    }
}
