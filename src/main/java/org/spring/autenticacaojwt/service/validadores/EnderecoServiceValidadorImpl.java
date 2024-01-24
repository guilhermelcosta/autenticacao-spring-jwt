package org.spring.autenticacaojwt.service.validadores;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.spring.autenticacaojwt.repository.EnderecoRepository;
import org.spring.autenticacaojwt.seguranca.UsuarioDetails;
import org.spring.autenticacaojwt.service.interfaces.Validador;

import java.util.UUID;

import static org.spring.autenticacaojwt.util.constantes.ConstantesTopicosUtil.ENDERECO_SERVICE;

@AllArgsConstructor
public class EnderecoServiceValidadorImpl implements Validador {

    private final EnderecoRepository enderecoRepository;

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
        return ENDERECO_SERVICE;
    }
}
