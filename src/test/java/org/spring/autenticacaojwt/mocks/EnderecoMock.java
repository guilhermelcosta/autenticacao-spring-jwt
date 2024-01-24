package org.spring.autenticacaojwt.mocks;

import lombok.experimental.UtilityClass;
import org.spring.autenticacaojwt.enums.UnidadeFederativa;
import org.spring.autenticacaojwt.model.Endereco;
import org.spring.autenticacaojwt.model.Usuario;

import static org.spring.autenticacaojwt.util.ConstantesEndereco.*;

@UtilityClass
public class EnderecoMock {

    /**
     * Cria um Endereco mock
     *
     * @return endereço mock
     */
    public static Endereco getEnderecoMock() {
        return Endereco.builder()
                .id(UUID_MOCK)
                .usuario(new Usuario())
                .rua(RUA_MOCK)
                .numero(NUM_MOCK)
                .bairro(BAIRRO_MOCK)
                .complemento(COMPLEMENTO_MOCK)
                .cep(CEP_MOCK)
                .cidade(CIDADE_MOCK)
                .estado(UnidadeFederativa.MINAS_GERAIS)
                .build();
    }
}
