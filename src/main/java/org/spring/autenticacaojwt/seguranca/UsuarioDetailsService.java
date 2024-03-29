package org.spring.autenticacaojwt.seguranca;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.autenticacaojwt.model.Usuario;
import org.spring.autenticacaojwt.service.interfaces.UsuarioService;
import org.spring.autenticacaojwt.enums.PerfilUsuario;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static org.spring.autenticacaojwt.util.constantes.ConstantesTopicosUtil.USUARIO_SPRING_SECURITY_SERVICE;


@Slf4j(topic = USUARIO_SPRING_SECURITY_SERVICE)
@Service
@AllArgsConstructor
public class UsuarioDetailsService implements UserDetailsService {

    private UsuarioService usuarioService;

    /**
     * Carrega informações do usuário
     *
     * @param email email do usuário
     * @return usuário da classe UsuarioDetails
     * @throws UsernameNotFoundException lança exceção caso o usuário não seja encontrado
     */
    @Override
    public UserDetails loadUserByUsername(String email) {
        log.info(">>> loadUserByUsername: carregando informações do usuário");
        Usuario usuario = usuarioService.encontrarPorEmail(email);
        return UsuarioDetails.builder()
                .id(usuario.getId())
                .email(usuario.getEmail())
                .senha(usuario.getSenha())
                .perfilUsuario(PerfilUsuario.getPerfilUsuario(usuario.getPerfilUsuario()))
                .build();
    }
}
