package org.spring.sistemaodontologia.seguranca;

import lombok.Builder;
import lombok.Getter;
import org.spring.sistemaodontologia.enums.PerfilUsuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Builder
@Getter
public class UsuarioDetails implements UserDetails {

    private UUID id;
    private String email;
    private String senha;
    private PerfilUsuario perfilUsuario;

    public UsuarioDetails(UUID id, String email, String senha, PerfilUsuario perfilUsuario) {
        this.id = id;
        this.email = email;
        this.senha = senha;
        this.perfilUsuario = perfilUsuario;
    }

    /**
     * Verifica se usuário é de um determinado perfil
     *
     * @param perfilUsuario perfil do usuário
     * @return boolean indicando se usuário é ou não de um determinado perfil
     */
    public boolean ehPerfil(PerfilUsuario perfilUsuario) {
        return this.perfilUsuario.equals(perfilUsuario);
    }

    /**
     * Obtém perfil do usuário ativo
     *
     * @return perfil do usuário ativo
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.perfilUsuario.getDescricao()));
    }

    /**
     * Obtém senha do usuário ativo
     *
     * @return senha do usuário ativo
     */
    @Override
    public String getPassword() {
        return this.senha;
    }

    /**
     * Obtém nome do usuário ativo
     *
     * @return nome do usuário ativo
     */
    @Override
    public String getUsername() {
        return this.email;
    }

    /**
     * Obtém indicação se a conta do usuário está expirada
     *
     * @return boolean indicando se a conta do usuário está expirada
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Obtém indicação se a conta do usuário está expirada
     *
     * @return boolean indicando se a conta do usuário está expirada
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Obtém indicação se a credencial do usuário está expirada
     *
     * @return boolean indicando se a credencial do usuário está expirada
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Obtém indicação se a conta do usuário está ativa
     *
     * @return boolean indicando se a conta do usuário está ativa
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
