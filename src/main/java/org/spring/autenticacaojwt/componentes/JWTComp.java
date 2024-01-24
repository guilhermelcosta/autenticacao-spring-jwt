package org.spring.autenticacaojwt.componentes;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

import static io.jsonwebtoken.Jwts.parserBuilder;
import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;
import static java.lang.System.currentTimeMillis;
import static java.util.Objects.nonNull;
import static org.spring.autenticacaojwt.util.constantes.ConstantesTopicosUtil.JWT_COMP;

@Slf4j(topic = JWT_COMP)
@Component
public class JWTComp {

    @Value("${jwt.segredo}")
    private String jwtSegredo;

    @Value("${jwt.tempo_expiracao}")
    private Long tempoExpiracao;

    /**
     * Gera um novo token de autenticação
     *
     * @param email email do usuário
     * @return token gerado
     */
    public String gerarToken(String email) {
        log.info(">>> gerarToken: gerando token de autenticação");
        SecretKey chave = gerarChaveSegredo();
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(currentTimeMillis() + tempoExpiracao))
                .signWith(chave)
                .compact();
    }

    /**
     * Gera segredo da chave do token
     *
     * @return segredo gerado
     */
    private @NotNull SecretKey gerarChaveSegredo() {
        return hmacShaKeyFor(this.jwtSegredo.getBytes());
    }

    /**
     * Verifica se um token é válido
     *
     * @param token token
     * @return boolean indicando se um token é válido ou não
     */
    public boolean tokenValido(String token) {
        log.info(">>> tokenValido: verificando validade do token");
        Claims claims = getClaims(token);
        if (nonNull(claims)) {
            String nome = claims.getSubject();
            Date dataExpiracaoToken = claims.getExpiration();
            Date dataAtual = new Date(currentTimeMillis());
            return nonNull(nome) && nonNull(dataExpiracaoToken) && dataAtual.before(dataExpiracaoToken);
        }
        log.info(">>> tokenValido: token expirado");
        return false;
    }

    /**
     * Obtém o email do usuário ativo
     *
     * @param token token
     * @return email do usuário ativo
     */
    public String getEmailUsuario(String token) {
        log.info(">>> getEmailUsuario: obtendo email do usuário ativo");
        Claims claims = getClaims(token);
        return nonNull(claims) ? claims.getSubject() : null;
    }


    /**
     * Obtém claims do usuário
     *
     * @param token token
     * @return claims do usuário
     */
    private @Nullable Claims getClaims(String token) {
        SecretKey chave = gerarChaveSegredo();
        try {
            return parserBuilder().setSigningKey(chave).build().parseClaimsJws(token).getBody();
        } catch (Exception e) {
            return null;
        }
    }
}
