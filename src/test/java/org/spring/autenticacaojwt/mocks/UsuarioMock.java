package org.spring.autenticacaojwt.mocks;

import lombok.experimental.UtilityClass;
import org.spring.autenticacaojwt.enums.PerfilUsuario;
import org.spring.autenticacaojwt.model.Endereco;
import org.spring.autenticacaojwt.model.Usuario;
import org.spring.autenticacaojwt.seguranca.UsuarioDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.spring.autenticacaojwt.util.ConstantesUsuario.*;

@UtilityClass
public class UsuarioMock {

    /**
     * Cria um Usuario mock
     *
     * @return usuário mock
     */
    public static Usuario getUsuarioMock() {
        return Usuario.builder()
                .id(UUID_MOCK)
                .endereco(new Endereco())
                .perfilUsuario(PerfilUsuario.USUARIO.getCodigo())
                .nome(NOME_MOCK)
                .rg(RG_MOCK)
                .cpf(CPF_MOCK)
                .dataNascimento(LocalDate.now())
                .email(EMAIL_MOCK)
                .senha(SENHA_MOCK)
                .telefone(TELEFONE_MOCK)
                .dataCriacao(LocalDateTime.now())
                .dataUltimaModificacao(LocalDateTime.now())
                .build();
    }

    /**
     * Cria um Usuario mock, com perfil ADMIN
     *
     * @return usuário mock, com perfil ADMIN
     */
    public static Usuario getUsuarioMockADMIN() {
        return Usuario.builder()
                .id(UUID_MOCK)
                .endereco(new Endereco())
                .perfilUsuario(PerfilUsuario.ADMIN.getCodigo())
                .nome(NOME_MOCK)
                .rg(RG_MOCK)
                .cpf(CPF_MOCK)
                .dataNascimento(LocalDate.now())
                .email(EMAIL_MOCK)
                .senha(SENHA_MOCK)
                .telefone(TELEFONE_MOCK)
                .dataCriacao(LocalDateTime.now())
                .dataUltimaModificacao(LocalDateTime.now())
                .build();
    }

    /**
     * Cria um usuarioDetails mock
     *
     * @return usuário details mock
     */
    public static UsuarioDetails getUsuarioDetailsMockNaoADMIN() {
        return UsuarioDetails.builder()
                .id(UUID_MOCK)
                .email(EMAIL_MOCK)
                .senha(SENHA_MOCK)
                .perfilUsuario(PerfilUsuario.USUARIO)
                .build();
    }

    /**
     * Cria um usuarioDetails mock, com perfil ADMIN
     *
     * @return usuário details mock, com perfil ADMIN
     */
    public static UsuarioDetails getUsuarioDetailsMockADMIN() {
        return UsuarioDetails.builder()
                .id(UUID_MOCK)
                .email(EMAIL_MOCK)
                .senha(SENHA_MOCK)
                .perfilUsuario(PerfilUsuario.ADMIN)
                .build();
    }
}
