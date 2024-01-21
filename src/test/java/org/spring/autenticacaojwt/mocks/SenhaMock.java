package org.spring.autenticacaojwt.mocks;

import lombok.experimental.UtilityClass;
import org.spring.autenticacaojwt.dto.SenhaDTO;

@UtilityClass
public class SenhaMock {

    /**
     * Cria uma SenhaDTO mock
     *
     * @return senha mock
     */
    public static SenhaDTO getSenhaMock() {
        return SenhaDTO.builder()
                .senhaOriginal("123456")
                .senhaAtualizada("1234567")
                .build();
    }
}
