package org.spring.autenticacaojwt.util;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class Constantes {

    public static final UUID UUID_MOCK = new UUID(1, 10);

    public static final String NOME_MOCK = "Usuário teste";

    public static final String RG_MOCK = "11.111-111";

    public static final String CPF_MOCK = "111.111.111-11";

    public static final String EMAIL_MOCK = "usuario@teste.com";

    public static final String SENHA_MOCK = "123456";

    public static final String SENHA_MOCK_ENCRIPTADA = "$2a$10$1hzfQKFoc0OxftqxbWdU/.xDtws6pyk9tiO2ll4kc31ZpDIsRrXi6";

    public static final String TELEFONE_MOCK = "11 91111-1111";

    public static final String PLANO_MOCK = "Plano teste";
}
