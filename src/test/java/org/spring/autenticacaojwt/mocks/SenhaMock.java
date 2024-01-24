package org.spring.autenticacaojwt.mocks;

import lombok.experimental.UtilityClass;
import org.spring.autenticacaojwt.dto.SenhaDTO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.spring.autenticacaojwt.util.ConstantesUsuario.SENHA_MOCK;
import static org.spring.autenticacaojwt.util.ConstantesUsuario.SENHA_MOCK_ATUALIZADA;

@UtilityClass
public class SenhaMock {

    /**
     * Cria uma SenhaDTO mock
     *
     * @return senha mock
     */
    public static SenhaDTO getSenhaMock() {
        return SenhaDTO.builder()
                .senhaOriginal(SENHA_MOCK)
                .senhaAtualizada(SENHA_MOCK_ATUALIZADA)
                .build();
    }

    /**
     * Encripta uma senha
     *
     * @return senha encriptada
     */
    public static String getSenhaMockEncriptada() {
        return new BCryptPasswordEncoder().encode(SENHA_MOCK);
    }
}
