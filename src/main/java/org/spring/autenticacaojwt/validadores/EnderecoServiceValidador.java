package org.spring.autenticacaojwt.validadores;

import org.jetbrains.annotations.NotNull;
import org.spring.autenticacaojwt.repository.EnderecoRepository;
import org.spring.autenticacaojwt.seguranca.UsuarioDetails;

import java.util.UUID;

import static org.spring.autenticacaojwt.util.constantes.ConstantesTopicosUtil.ENDERECO_SERVICE;

public class EnderecoServiceValidador implements Validador {

    private static final String TOPICO_VALIDADOR = ENDERECO_SERVICE;
    private final EnderecoRepository enderecoRepository;

    public EnderecoServiceValidador(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    /**
     * Valida um usuário para efetuar uma requisição
     *
     * @param idEndereco     id do endereço relativo à requisição
     * @param usuarioDetails objeto do tipo usuarioDetails
     * @return boolean indicando se o usuário foi ou não validado
     */
    @Override
    public boolean validar(UUID idEndereco, @NotNull UsuarioDetails usuarioDetails) {
        UUID idEnderecoUsuarioAtual = enderecoRepository.buscarEnderecoPorIdUsuario(usuarioDetails.getId()).getId();
        return idEnderecoUsuarioAtual.equals(idEndereco);
    }

    /**
     * Obtém o tópico do validador
     *
     * @return tópico
     */
    @Override
    public String getTopico() {
        return TOPICO_VALIDADOR;
    }
}
