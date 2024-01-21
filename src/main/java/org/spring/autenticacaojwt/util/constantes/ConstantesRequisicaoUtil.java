package org.spring.autenticacaojwt.util.constantes;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UtilityClass
public class ConstantesRequisicaoUtil {

    public static final String[] CAMINHOS_PUBLICOS = {"/", "/actuator/health"};

    public static final String[] CAMINHOS_PUBLICOS_POST = {"/usuario", "/login"};

    public static final List<String> METODOS_PERMITIDOS_CORS = new ArrayList<>(Arrays.asList("POST", "GET", "PUT", "DELETE"));

    public static final String HEADER_AUTORIZACAO = "Authorization";

    public static final String VALOR_HEADER_AUTORIZACAO = "Bearer %s";

    public static final String TIPO_TOKEN = "Bearer";

    public static final String CONTENT_TYPE = "application/json";

    public static final String CHARACTER_ENCODING = "UTF-8";

    public static final String CORPO_RESPOSTA_REQUISICAO = "{" +
            "\n\"token\": \"" + TIPO_TOKEN + " %s\"," +
            "\n\"usuario\": \"%s\"" + "," +
            "\n\"hora\": \"%s\"" + "," +
            "\n}";
}
