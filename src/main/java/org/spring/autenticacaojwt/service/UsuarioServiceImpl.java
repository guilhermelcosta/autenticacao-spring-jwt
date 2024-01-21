package org.spring.autenticacaojwt.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.spring.autenticacaojwt.dto.SenhaDTO;
import org.spring.autenticacaojwt.enums.PerfilUsuario;
import org.spring.autenticacaojwt.excecao.lancaveis.AtualizarSenhaException;
import org.spring.autenticacaojwt.excecao.lancaveis.DeletarEntidadeException;
import org.spring.autenticacaojwt.excecao.lancaveis.EntidadeNaoEncontradaException;
import org.spring.autenticacaojwt.model.Usuario;
import org.spring.autenticacaojwt.repository.UsuarioRepository;
import org.spring.autenticacaojwt.seguranca.UsuarioDetails;
import org.spring.autenticacaojwt.service.interfaces.OperacoesCRUDService;
import org.spring.autenticacaojwt.service.interfaces.UsuarioService;
import org.spring.autenticacaojwt.service.interfaces.ValidadorAutorizacaoRequisicaoService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.spring.autenticacaojwt.util.constantes.ConstantesPropIgnoradasUtil.PROPRIEDADES_IGNORADAS_USUARIO;
import static org.spring.autenticacaojwt.util.constantes.ConstantesTopicosUtil.USUARIO_SERVICE;
import static org.springframework.beans.BeanUtils.copyProperties;


@Slf4j(topic = USUARIO_SERVICE)
@Service
public class UsuarioServiceImpl implements OperacoesCRUDService<Usuario>, UsuarioService {

    private final ValidadorAutorizacaoRequisicaoService validadorAutorizacaoRequisicaoService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoderImpl passwordEncoder;

    public UsuarioServiceImpl(ValidadorAutorizacaoRequisicaoService validadorAutorizacaoRequisicaoService, UsuarioRepository usuarioRepository) {
        this.validadorAutorizacaoRequisicaoService = validadorAutorizacaoRequisicaoService;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = PasswordEncoderImpl.getInstancia();
    }

    /**
     * Encontra um usuário a partir do seu id
     *
     * @param id id do usuário
     * @return usuário encontrado
     */
    @Override
    public Usuario encontrarPorId(@NotNull UUID id) {
        validadorAutorizacaoRequisicaoService.validarAutorizacaoRequisicao(id, USUARIO_SERVICE);
        log.info(">>> encontrarPorId: encontrando usuário por id");
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(String.format("usuário não encontrado, id: %s", id)));
    }

    /**
     * Encontra um usuário a partir do seu email
     *
     * @param email email do usuário
     * @return usuário encontrado
     */
    @Override
    public Usuario encontrarPorEmail(@NotNull String email) {
        log.info(">>> encontrarPorEmail: encontrando usuário por email");
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(String.format("usuário não encontrado, email: %s", email)));
    }

    /**
     * Lista todos os usuários criados
     *
     * @return lista de usuários
     */
    @Override
    public List<Usuario> listarTodos() {
        validadorAutorizacaoRequisicaoService.validarAutorizacaoRequisicao();
        log.info(">>> listarTodos: listando todos usuários");
        return usuarioRepository.findAll()
                .stream()
                .toList();
    }

    /**
     * Cria um novo usuário
     *
     * @param usuario objeto do tipo Usuario
     * @return novo usuário criado
     */
    @Override
    @Transactional
    public Usuario criar(@NotNull Usuario usuario) {
        log.info(">>> criar: criando usuário");
        usuario.setId(null);
        usuario.setDataCriacao(LocalDateTime.now());
        usuario.setDataUltimaModificacao(LocalDateTime.now());
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario.setPerfilUsuario(PerfilUsuario.USUARIO.getCodigo());
        usuario = usuarioRepository.save(usuario);
        log.info(String.format(">>> criar: usuário criado, id: %s", usuario.getId()));
        return usuario;
    }

    /**
     * Atualiza um usuário previamente cadastrado
     *
     * @param usuario objeto do tipo Usuario (informações atualizadas)
     * @return novo usuário atualizado
     */
    @Override
    public Usuario atualizar(@NotNull Usuario usuario) {
        UsuarioDetails usuarioDetails = validadorAutorizacaoRequisicaoService.validarAutorizacaoRequisicao(usuario.getId(), USUARIO_SERVICE);
        log.info(">>> atualizar: atualizando usuário");
        Usuario usuarioAtualizado = encontrarPorId(usuario.getId());
        copyProperties(usuario, usuarioAtualizado, PROPRIEDADES_IGNORADAS_USUARIO);
        usuarioAtualizado.setDataUltimaModificacao(LocalDateTime.now());
        if (usuarioDetails.ehPerfil(PerfilUsuario.ADMIN))
            usuarioAtualizado.setPerfilUsuario(usuario.getPerfilUsuario());
        usuarioAtualizado = usuarioRepository.save(usuarioAtualizado);
        log.info(String.format(">>> atualizar: usuário atualizado, id: %s", usuarioAtualizado.getId()));
        return usuarioAtualizado;
    }

    /**
     * Delete um usuário a partir do seu id
     *
     * @param id id do usuário
     */
    @Override
    public void deletar(@NotNull UUID id) {
        validadorAutorizacaoRequisicaoService.validarAutorizacaoRequisicao(id, USUARIO_SERVICE);
        log.info(">>> deletar: deletando usuário");
        encontrarPorId(id);
        try {
            this.usuarioRepository.deleteById(id);
            log.info(String.format(">>> deletar: usuário deletado, id: %s", id));
        } catch (Exception e) {
            throw new DeletarEntidadeException(String.format("existem entidades relacionadas: %s", e));
        }
    }

    /**
     * Atualiza senha do usuário
     *
     * @param id       id do usuário
     * @param senhaDTO objeto do tipo SenhaDTO
     */
    @Override
    public void atualizarSenha(@NotNull UUID id, @NotNull SenhaDTO senhaDTO) {
        validadorAutorizacaoRequisicaoService.validarAutorizacaoRequisicao(id, USUARIO_SERVICE);
        Usuario usuario = encontrarPorId(id);
        if (passwordEncoder.matches(senhaDTO.senhaOriginal(), usuarioRepository.buscarSenhaUsuarioPorId(usuario.getId())))
            usuarioRepository.atualizarSenhaUsuario(passwordEncoder.encode(senhaDTO.senhaAtualizada()), id);
        else
            throw new AtualizarSenhaException(String.format("senha original incorreta, id do usuário: %s", id));
    }
}
