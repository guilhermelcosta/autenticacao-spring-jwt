package org.spring.autenticacaojwt.service;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.spring.autenticacaojwt.excecao.lancaveis.EntidadeNaoEncontradaException;
import org.spring.autenticacaojwt.model.Endereco;
import org.spring.autenticacaojwt.repository.EnderecoRepository;
import org.spring.autenticacaojwt.service.interfaces.OperacoesCRUDService;
import org.spring.autenticacaojwt.service.interfaces.ValidadorAutorizacaoRequisicaoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static org.spring.autenticacaojwt.util.constantes.ConstantesTopicosUtil.ENDERECO_SERVICE;
import static org.springframework.beans.BeanUtils.copyProperties;

@Slf4j(topic = ENDERECO_SERVICE)
@Service
public class EnderecoServiceImpl implements OperacoesCRUDService<Endereco> {

    private final ValidadorAutorizacaoRequisicaoService validadorAutorizacaoRequisicaoService;
    private final EnderecoRepository enderecoRepository;

    public EnderecoServiceImpl(ValidadorAutorizacaoRequisicaoService validadorAutorizacaoRequisicaoService, EnderecoRepository enderecoRepository) {
        this.validadorAutorizacaoRequisicaoService = validadorAutorizacaoRequisicaoService;
        this.enderecoRepository = enderecoRepository;
    }

    /**
     * Encontra um endereço a partir do seu id
     *
     * @param id id do endereço
     * @return endereço encontrado
     */
    @Override
    public Endereco encontrarPorId(@NotNull UUID id) {
        validadorAutorizacaoRequisicaoService.validarAutorizacaoRequisicao(id, ENDERECO_SERVICE);
        log.info(">>> encontrarPorId: encontrando endereço por id");
        return enderecoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(String.format("endereço não encontrado, id: %s", id)));
    }

    /**
     * Lista todos os endereços criados
     *
     * @return lista de endereços
     */
    @Override
    public List<Endereco> listarTodos() {
        validadorAutorizacaoRequisicaoService.validarAutorizacaoRequisicao();
        log.info(">>> listarTodos: listando todos endereços");
        return enderecoRepository.findAll()
                .stream()
                .toList();
    }

    /**
     * Cria um novo endereço
     *
     * @param endereco objeto do tipo Endereco
     * @return novo endereço criado
     */
    @Override
    public Endereco criar(@NotNull Endereco endereco) {
        validadorAutorizacaoRequisicaoService.validarAutorizacaoRequisicao();
        log.info(">>> criar: criando endereço");
        endereco.setId(null);
        endereco = enderecoRepository.save(endereco);
        log.info(String.format(">>> criar: endereço criado, id: %s", endereco.getId()));
        return endereco;
    }

    /**
     * Atualiza um endereço previamente cadastrado
     *
     * @param endereco objeto do tipo Endereco (informações atualizadas)
     * @return novo endereço atualizado
     */
    @Override
    public Endereco atualizar(@NotNull Endereco endereco) {
        validadorAutorizacaoRequisicaoService.validarAutorizacaoRequisicao(endereco.getId(), ENDERECO_SERVICE);
        log.info(">>> atualizar: atualizando endereço");
        Endereco enderecoAtualizado = encontrarPorId(endereco.getId());
        copyProperties(endereco, enderecoAtualizado);
        enderecoAtualizado = enderecoRepository.save(enderecoAtualizado);
        log.info(String.format(">>> atualizar: endereço atualizado, id: %s", enderecoAtualizado.getId()));
        return enderecoAtualizado;
    }

    /**
     * Delete um endereço a partir do seu id
     *
     * @param id id do endereço
     */
    @Override
    public void deletar(UUID id) {

    }
}
