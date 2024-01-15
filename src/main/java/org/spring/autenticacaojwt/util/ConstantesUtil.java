package org.spring.autenticacaojwt.util;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UtilityClass
public class ConstantesUtil {

    public static final List<String> PROPRIEDADES_ADMIN = new ArrayList<>(Arrays.asList("id", "perfilUsuario"));

    /*Endpoints requisições*/

    public static final String ENDPOINT_USUARIO = "/usuario";

    /*Rotas para configuração do SegurancaConfig*/

    public static final String[] CAMINHOS_PUBLICOS = {"/", "/actuator/health"};

    public static final String[] CAMINHOS_PUBLICOS_POST = {"/usuario", "/login"};

    public static final List<String> METODOS_PERMITIDOS_CORS = new ArrayList<>(Arrays.asList("POST", "GET", "PUT", "DELETE"));

    /*Configuração da resposta e headers da requisição de autenticação e autorização*/

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

    /*Mensagens de erro de validação*/

    public static final String MSG_ERRO_USUARIO_SENHA = "nome de usuário ou senha inválidos";

    public static final String MSG_ERRO_VALIDACAO = "erro de validação, verifique o log para detalhes";

    public static final String MSG_ERRO_RG = "formato esperado: 00.000-000";

    public static final String MSG_ERRO_CPF = "formato esperado: 000.000.000-00";

    public static final String MSG_ERRO_EMAIL = "formato esperado: usuario@email.com.br ou usuario@email.com";

    public static final String MSG_ERRO_SENHA = "a senha deve conter no mínimo 6 caracteres";

    public static final String MSG_ERRO_TELEFONE = "formato esperado: 00 00000-0000";

    public static final String MSG_ERRO_CEP = "formato esperado: 00000-000";

    /*Números*/

    public static final Integer NUM_UM = 1;

    public static final Integer NUM_DOIS = 2;

    public static final Integer NUM_SEIS = 6;

    public static final Integer NUM_VINTE_E_CINCO = 25;

    public static final Integer NUM_CEM = 100;

    public static final Integer NUM_DUZENTOS = 200;

    /*Enums*/

    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    public static final String ROLE_USUARIO = "ROLE_USUARIO";

}
