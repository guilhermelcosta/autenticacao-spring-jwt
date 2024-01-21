package org.spring.sistemaodontologia.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.spring.sistemaodontologia.dto.SenhaDTO;
import org.spring.sistemaodontologia.excecao.lancaveis.AtualizarSenhaException;
import org.spring.sistemaodontologia.excecao.lancaveis.DeletarEntidadeException;
import org.spring.sistemaodontologia.excecao.lancaveis.EntidadeNaoEncontradaException;
import org.spring.sistemaodontologia.model.Usuario;
import org.spring.sistemaodontologia.repository.UsuarioRepository;
import org.spring.sistemaodontologia.service.interfaces.ValidadorAutorizacaoRequisicaoService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.spring.sistemaodontologia.mocks.SenhaMock.getSenhaMock;
import static org.spring.sistemaodontologia.mocks.UsuarioMock.*;
import static org.spring.sistemaodontologia.util.Constantes.*;
import static org.spring.sistemaodontologia.util.constantes.ConstantesNumUtil.DOIS;
import static org.spring.sistemaodontologia.util.constantes.ConstantesTopicosUtil.USUARIO_SERVICE;

@ExtendWith(SpringExtension.class)
public class UsuarioServiceImplTest {

    @InjectMocks
    private UsuarioServiceImpl usuarioServiceImpl;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ValidadorAutorizacaoRequisicaoService validadorAutorizacaoRequisicaoService;

    @Mock
    private PasswordEncoderImpl passwordEncoder;

    @Test
    @DisplayName("testCriar: usuário válido -> deve retornar usuário")
    void testCriar_usuarioValido_deveRetornarUsuario() {
        // Given
        Usuario usuarioEnviadoRequisicao = getUsuarioMock();

        // When
        when(usuarioRepository.save(usuarioEnviadoRequisicao)).thenReturn(usuarioEnviadoRequisicao);
        Usuario usuarioCriado = usuarioServiceImpl.criar(usuarioEnviadoRequisicao);

        // Then
        assertSame(usuarioCriado, usuarioEnviadoRequisicao);
    }

    @Test
    @DisplayName("testCriar: usuário inválido -> deve lançar DataIntegrityViolationException")
    void testCriar_usuarioInvalido_deveLancarDataIntegrityViolationException() {
        // Given
        Usuario usuario = getUsuarioMock();

        // When
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        when(usuarioServiceImpl.criar(usuario)).thenThrow(DataIntegrityViolationException.class);

        // Then
        assertThrows(DataIntegrityViolationException.class, () -> usuarioServiceImpl.criar(usuario));
    }

    @Test
    @DisplayName("testEncontrarPorId: id válido -> deve retornar usuário")
    void testEncontrarPorId_idValido_deveRetornarUsuario() {
        // Given
        Usuario usuarioBuscado = getUsuarioMock();

        // When
        when(usuarioRepository.findById(UUID_MOCK)).thenReturn(ofNullable(usuarioBuscado));
        Usuario usuarioEncontrado = usuarioServiceImpl.encontrarPorId(UUID_MOCK);

        // Then
        assertNotNull(usuarioEncontrado);
        assertEquals(usuarioEncontrado.getId(), requireNonNull(usuarioBuscado).getId());
    }

    @Test
    @DisplayName("testEncontrarPorId: id inválido -> deve lançar EntidadeNaoEncontradaException")
    void testEncontrarPorId_idInvalido_deveLancarEntidadeNaoEncontradaException() {
        // Given e When
        when(usuarioRepository.findById(UUID_MOCK)).thenReturn(empty());

        // Then
        assertThrows(EntidadeNaoEncontradaException.class, () -> usuarioServiceImpl.encontrarPorId(UUID_MOCK));
    }

    @Test
    @DisplayName("testEncontrarPorEmail: email válido -> deve retornar usuário")
    void testEncontrarPorEmail_emailValido_deveRetornarUsuario() {
        // Given
        Usuario usuarioBuscado = getUsuarioMock();

        // When
        when(usuarioRepository.findByEmail(EMAIL_MOCK)).thenReturn(ofNullable(usuarioBuscado));
        Usuario usuarioEncontrado = usuarioServiceImpl.encontrarPorEmail(EMAIL_MOCK);

        // Then
        assertNotNull(usuarioEncontrado);
        assertEquals(usuarioEncontrado.getId(), requireNonNull(usuarioBuscado).getId());
    }

    @Test
    @DisplayName("testEncontrarPorEmail: email inválido -> deve lançar EntidadeNaoEncontradaException")
    void testEncontrarPorEmail_emailInvalido_deveLancarEntidadeNaoEncontradaException() {
        // Given e When
        when(usuarioRepository.findByEmail(EMAIL_MOCK)).thenReturn(empty());

        // Then
        assertThrows(EntidadeNaoEncontradaException.class, () -> usuarioServiceImpl.encontrarPorEmail(EMAIL_MOCK));
    }

    @Test
    @DisplayName("testListarTodos: possui usuários cadastrados -> deve listar usuários")
    void testListarTodos_possuiUsuariosCadastrados_deveListarUsuarios() {
        // Given
        List<Usuario> usuariosCadastrados = Arrays.asList(getUsuarioMock(), getUsuarioMock());

        // When
        when(usuarioRepository.findAll()).thenReturn(usuariosCadastrados);
        List<Usuario> usuariosRecuperados = usuarioServiceImpl.listarTodos();

        // Then
        assertEquals(DOIS, usuariosRecuperados.size());
        assertArrayEquals(usuariosRecuperados.toArray(), usuariosCadastrados.toArray());
    }

    @Test
    @DisplayName("testAtualizar: usuário válido que não possui perfil ADMIN -> deve retornar usuário atualizado")
    void testAtualizar_usuarioValidoNaoADMIN_deveRetornarUsuarioAtualizado() {
        // Given
        Usuario usuarioAtualizadoEnviadoRequisicao = getUsuarioMock();

        // When
        when(validadorAutorizacaoRequisicaoService.validarAutorizacaoRequisicao(UUID_MOCK, USUARIO_SERVICE)).thenReturn(getUsuarioDetailsMockNaoADMIN());
        when(usuarioRepository.findById(UUID_MOCK)).thenReturn(ofNullable(usuarioAtualizadoEnviadoRequisicao));
        when(usuarioRepository.save(requireNonNull(usuarioAtualizadoEnviadoRequisicao))).thenReturn(usuarioAtualizadoEnviadoRequisicao);
        Usuario usuarioAtualizado = usuarioServiceImpl.atualizar(usuarioAtualizadoEnviadoRequisicao);

        // Then
        assertNotNull(usuarioAtualizado);
        assertSame(usuarioAtualizado, usuarioAtualizadoEnviadoRequisicao);
    }

    @Test
    @DisplayName("testAtualizar: usuário válido que possui perfil ADMIN -> deve retornar usuário atualizado")
    void testAtualizar_usuarioValidoEhADMIN_deveRetornarUsuarioAtualizado() {
        // Given
        Usuario usuarioAtualizadoEnviadoRequisicao = getUsuarioMockADMIN();

        // When
        when(validadorAutorizacaoRequisicaoService.validarAutorizacaoRequisicao(UUID_MOCK, USUARIO_SERVICE)).thenReturn(getUsuarioDetailsMockADMIN());
        when(usuarioRepository.findById(UUID_MOCK)).thenReturn(ofNullable(usuarioAtualizadoEnviadoRequisicao));
        when(usuarioRepository.save(requireNonNull(usuarioAtualizadoEnviadoRequisicao))).thenReturn(usuarioAtualizadoEnviadoRequisicao);
        Usuario usuarioAtualizado = usuarioServiceImpl.atualizar(usuarioAtualizadoEnviadoRequisicao);

        // Then
        assertNotNull(usuarioAtualizado);
        assertSame(usuarioAtualizado, usuarioAtualizadoEnviadoRequisicao);
    }

    @Test
    @DisplayName("testAtualizar: usuário inválido -> deve lançar DataIntegrityViolationException")
    void testAtualizar_usuarioInvalido_deveLancarDataIntegrityViolationException() {
        // Given
        Usuario usuarioAtualizadoEnviadoRequisicao = getUsuarioMockADMIN();

        // When
        when(validadorAutorizacaoRequisicaoService.validarAutorizacaoRequisicao(UUID_MOCK, USUARIO_SERVICE)).thenReturn(getUsuarioDetailsMockADMIN());
        when(usuarioRepository.findById(UUID_MOCK)).thenReturn(ofNullable(usuarioAtualizadoEnviadoRequisicao));
        when(usuarioRepository.save(requireNonNull(usuarioAtualizadoEnviadoRequisicao))).thenThrow(DataIntegrityViolationException.class);

        // Then
        assertThrows(DataIntegrityViolationException.class, () -> usuarioServiceImpl.atualizar(usuarioAtualizadoEnviadoRequisicao));
    }

    @Test
    @DisplayName("testDeletar: usuário válido -> deve deletar usuário")
    void testDeletar_usuarioValido_deveDeletarUsuario() {
        // Given
        Usuario usuarioDeletado = getUsuarioMock();

        // When
        when(usuarioRepository.findById(UUID_MOCK)).thenReturn(ofNullable(usuarioDeletado));

        // Then
        assertDoesNotThrow(() -> usuarioServiceImpl.deletar(requireNonNull(usuarioDeletado).getId()));
    }

    @Test
    @DisplayName("testDeletar: falha ao deletar usuário -> deve lançar DeletarEntidadeException")
    void testDeletar_falhaAoDeletarUsuario_deveLancarDeletarEntidadeException() {
        // Given
        Usuario usuarioDeletado = getUsuarioMock();

        // When
        when(usuarioRepository.findById(UUID_MOCK)).thenReturn(ofNullable(usuarioDeletado));
        doThrow(DeletarEntidadeException.class).when(usuarioRepository).deleteById(UUID_MOCK);

        // Then
        assertThrows(DeletarEntidadeException.class, () -> usuarioServiceImpl.deletar(requireNonNull(usuarioDeletado).getId()));
    }

    @Test
    @DisplayName("testAtualizarSenha: senhas compatíveis -> deve atualizar senha")
    void testAtualizarSenha_senhasCompativeis_deveAtualizarSenha() {
        // Given
        Usuario usuario = getUsuarioMock();
        SenhaDTO senhaEnviadaRequisicao = getSenhaMock();


        // When
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(SENHA_MOCK, SENHA_MOCK)).thenReturn(true);
        when(usuarioRepository.buscarSenhaUsuarioPorId(usuario.getId())).thenReturn(SENHA_MOCK_ENCRIPTADA);

        // Then
        assertDoesNotThrow(() -> usuarioServiceImpl.atualizarSenha(usuario.getId(), senhaEnviadaRequisicao));
    }

    @Test
    @DisplayName("testAtualizarSenha: senhas incompatíveis -> deve lançar AtualizarSenhaException")
    void testAtualizarSenha_senhasNaoCompativeis_deveLancarAtualizarSenhaException() {
        // Given
        Usuario usuario = getUsuarioMock();
        SenhaDTO senhaEnviadaRequisicao = getSenhaMock();


        // When
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(SENHA_MOCK, SENHA_MOCK)).thenReturn(false);
        when(usuarioRepository.buscarSenhaUsuarioPorId(usuario.getId())).thenReturn(SENHA_MOCK);

        // Then
        assertThrows(AtualizarSenhaException.class, () -> usuarioServiceImpl.atualizarSenha(usuario.getId(), senhaEnviadaRequisicao));
    }
}
