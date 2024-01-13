package org.spring.autenticacaojwt.service;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.spring.autenticacaojwt.dto.EnderecoDTO;
import org.spring.autenticacaojwt.excecoes.lancaveis.EntidadeNaoEncontradaException;
import org.spring.autenticacaojwt.repository.EnderecoRepository;
import org.spring.autenticacaojwt.service.interfaces.EnderecoService;
import org.spring.autenticacaojwt.util.ConversorEntidadeDTOUtil;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j(topic = "ENDERECO_SERVICE")
@Service
public class EnderecoServiceImpl implements EnderecoService {

    private final EnderecoRepository enderecoRepository;

    public EnderecoServiceImpl(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    /**
     * Encontra um endereço a partir do seu id
     *
     * @param id id do endereço
     * @return endereço encontrado
     */
    @Override
    public EnderecoDTO encontrarPorId(@NotNull UUID id) {
        log.info(">>> encontrarPorId: encontrando endereço por id");
        return enderecoRepository.findById(id)
                .map(ConversorEntidadeDTOUtil::converterParaDTO)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(String.format("endereço não encontrado, id: %s", id)));
    }
}
