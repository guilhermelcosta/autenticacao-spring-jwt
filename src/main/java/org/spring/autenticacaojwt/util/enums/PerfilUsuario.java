package org.spring.autenticacaojwt.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

import static org.spring.autenticacaojwt.util.ConstantesUtil.*;

@Getter
@AllArgsConstructor
public enum PerfilUsuario {

    ADMIN(NUM_UM, ROLE_ADMIN),
    USUARIO(NUM_DOIS, ROLE_USUARIO);

    private final Integer codigo;
    private final String descricao;

    /**
     * Obtém o objeto PerfilUsuario relacionado a um código inteiro
     *
     * @param codigo código do perfil
     * @return objeto PerfilUsuario relacionado ao código inteiro
     */
    public static PerfilUsuario getPerfilUsuario(int codigo) {
        return Arrays.stream(PerfilUsuario.values())
                .filter(perfil -> perfil.codigo == codigo)
                .findFirst()
                .orElse(null);
    }
}
