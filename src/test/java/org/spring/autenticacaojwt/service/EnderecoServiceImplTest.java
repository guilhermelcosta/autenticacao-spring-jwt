package org.spring.autenticacaojwt.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.spring.autenticacaojwt.excecao.lancaveis.DeletarEntidadeException;
import org.spring.autenticacaojwt.excecao.lancaveis.EntidadeNaoEncontradaException;
import org.spring.autenticacaojwt.model.Endereco;
import org.spring.autenticacaojwt.repository.EnderecoRepository;
import org.spring.autenticacaojwt.service.interfaces.ValidadorAutorizacaoRequisicaoService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.spring.autenticacaojwt.mocks.EnderecoMock.getEnderecoMock;
import static org.spring.autenticacaojwt.mocks.UsuarioMock.getUsuarioDetailsMockADMIN;
import static org.spring.autenticacaojwt.mocks.UsuarioMock.getUsuarioDetailsMockNaoADMIN;
import static org.spring.autenticacaojwt.util.ConstantesUsuario.UUID_MOCK;
import static org.spring.autenticacaojwt.util.constantes.ConstantesNumUtil.DOIS;
import static org.spring.autenticacaojwt.util.constantes.ConstantesTopicosUtil.ENDERECO_SERVICE;

@ExtendWith(SpringExtension.class)
public class EnderecoServiceImplTest {

    @InjectMocks
    private EnderecoServiceImpl enderecoService;

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private ValidadorAutorizacaoRequisicaoService validadorAutorizacaoRequisicaoService;

    @Test
    @DisplayName("testCriar: endereço válido -> deve retornar endereço")
    void testCriar_enderecoValido_deveRetornarUsuario() {
        // Given
        Endereco enderecoEnviadoRequisicao = getEnderecoMock();

        // When
        when(enderecoRepository.save(enderecoEnviadoRequisicao)).thenReturn(enderecoEnviadoRequisicao);
        Endereco enderecoCriado = enderecoService.criar(enderecoEnviadoRequisicao);

        // Then
        assertSame(enderecoCriado, enderecoEnviadoRequisicao);
    }

    @Test
    @DisplayName("testCriar: endereço inválido -> deve lançar DataIntegrityViolationException")
    void testCriar_enderecoInvalido_deveLancarDataIntegrityViolationException() {
        // Given
        Endereco endereco = getEnderecoMock();

        // When
        when(enderecoRepository.save(endereco)).thenReturn(endereco);
        when(enderecoService.criar(endereco)).thenThrow(DataIntegrityViolationException.class);

        // Then
        assertThrows(DataIntegrityViolationException.class, () -> enderecoService.criar(endereco));
    }

    @Test
    @DisplayName("testEncontrarPorId: id válido -> deve retornar endereço")
    void testEncontrarPorId_idValido_deveRetornarUsuario() {
        // Given
        Endereco enderecoBuscado = getEnderecoMock();

        // When
        when(enderecoRepository.findById(UUID_MOCK)).thenReturn(ofNullable(enderecoBuscado));
        Endereco enderecoEncontrado = enderecoService.encontrarPorId(UUID_MOCK);

        // Then
        assertNotNull(enderecoEncontrado);
        assertEquals(enderecoEncontrado.getId(), requireNonNull(enderecoBuscado).getId());
    }

    @Test
    @DisplayName("testEncontrarPorId: id inválido -> deve lançar EntidadeNaoEncontradaException")
    void testEncontrarPorId_idInvalido_deveLancarEntidadeNaoEncontradaException() {
        // Given e When
        when(enderecoRepository.findById(UUID_MOCK)).thenReturn(empty());

        // Then
        assertThrows(EntidadeNaoEncontradaException.class, () -> enderecoService.encontrarPorId(UUID_MOCK));
    }

    @Test
    @DisplayName("testListarTodos: possui endereços cadastrados -> deve listar endereços")
    void testListarTodos_possuiUsuariosCadastrados_deveListarUsuarios() {
        // Given
        List<Endereco> enderecosCadastrados = asList(getEnderecoMock(), getEnderecoMock());

        // When
        when(enderecoRepository.findAll()).thenReturn(enderecosCadastrados);
        List<Endereco> enderecosRecuperados = enderecoService.listarTodos();

        // Then
        assertEquals(DOIS, enderecosRecuperados.size());
        assertArrayEquals(enderecosRecuperados.toArray(), enderecosCadastrados.toArray());
    }

    @Test
    @DisplayName("testAtualizar: endereço válido que não possui perfil ADMIN -> deve retornar endereço atualizado")
    void testAtualizar_enderecoValidoNaoADMIN_deveRetornarUsuarioAtualizado() {
        // Given
        Endereco enderecoAtualizadoEnviadoRequisicao = getEnderecoMock();

        // When
        when(validadorAutorizacaoRequisicaoService.validarAutorizacaoRequisicao(UUID_MOCK, ENDERECO_SERVICE)).thenReturn(getUsuarioDetailsMockNaoADMIN());
        when(enderecoRepository.findById(UUID_MOCK)).thenReturn(ofNullable(enderecoAtualizadoEnviadoRequisicao));
        when(enderecoRepository.save(requireNonNull(enderecoAtualizadoEnviadoRequisicao))).thenReturn(enderecoAtualizadoEnviadoRequisicao);
        Endereco enderecoAtualizado = enderecoService.atualizar(enderecoAtualizadoEnviadoRequisicao);

        // Then
        assertNotNull(enderecoAtualizado);
        assertSame(enderecoAtualizado, enderecoAtualizadoEnviadoRequisicao);
    }

    @Test
    @DisplayName("testAtualizar: endereço válido que possui perfil ADMIN -> deve retornar endereço atualizado")
    void testAtualizar_enderecoValidoEhADMIN_deveRetornarUsuarioAtualizado() {
        // Given
        Endereco enderecoAtualizadoEnviadoRequisicao = getEnderecoMock();

        // When
        when(validadorAutorizacaoRequisicaoService.validarAutorizacaoRequisicao(UUID_MOCK, ENDERECO_SERVICE)).thenReturn(getUsuarioDetailsMockADMIN());
        when(enderecoRepository.findById(UUID_MOCK)).thenReturn(ofNullable(enderecoAtualizadoEnviadoRequisicao));
        when(enderecoRepository.save(requireNonNull(enderecoAtualizadoEnviadoRequisicao))).thenReturn(enderecoAtualizadoEnviadoRequisicao);
        Endereco enderecoAtualizado = enderecoService.atualizar(enderecoAtualizadoEnviadoRequisicao);

        // Then
        assertNotNull(enderecoAtualizado);
        assertSame(enderecoAtualizado, enderecoAtualizadoEnviadoRequisicao);
    }

    @Test
    @DisplayName("testAtualizar: endereço inválido -> deve lançar DataIntegrityViolationException")
    void testAtualizar_enderecoInvalido_deveLancarDataIntegrityViolationException() {
        // Given
        Endereco enderecoAtualizadoEnviadoRequisicao = getEnderecoMock();

        // When
        when(validadorAutorizacaoRequisicaoService.validarAutorizacaoRequisicao(UUID_MOCK, ENDERECO_SERVICE)).thenReturn(getUsuarioDetailsMockADMIN());
        when(enderecoRepository.findById(UUID_MOCK)).thenReturn(ofNullable(enderecoAtualizadoEnviadoRequisicao));
        when(enderecoRepository.save(requireNonNull(enderecoAtualizadoEnviadoRequisicao))).thenThrow(DataIntegrityViolationException.class);

        // Then
        assertThrows(DataIntegrityViolationException.class, () -> enderecoService.atualizar(enderecoAtualizadoEnviadoRequisicao));
    }

    @Test
    @DisplayName("testDeletar: endereço válido -> deve deletar endereço")
    void testDeletar_enderecoValido_deveDeletarUsuario() {
        // Given
        Endereco enderecoDeletado = getEnderecoMock();

        // When
        when(enderecoRepository.findById(UUID_MOCK)).thenReturn(ofNullable(enderecoDeletado));

        // Then
        assertDoesNotThrow(() -> enderecoService.deletar(requireNonNull(enderecoDeletado).getId()));
    }

    @Test
    @DisplayName("testDeletar: falha ao deletar endereço -> deve lançar DeletarEntidadeException")
    void testDeletar_falhaAoDeletarUsuario_deveLancarDeletarEntidadeException() {
        // Given
        Endereco enderecoDeletado = getEnderecoMock();

        // When
        when(enderecoRepository.findById(UUID_MOCK)).thenReturn(ofNullable(enderecoDeletado));
        doThrow(DeletarEntidadeException.class).when(enderecoRepository).deleteById(UUID_MOCK);

        // Then
        assertThrows(DeletarEntidadeException.class, () -> enderecoService.deletar(requireNonNull(enderecoDeletado).getId()));
    }
}
