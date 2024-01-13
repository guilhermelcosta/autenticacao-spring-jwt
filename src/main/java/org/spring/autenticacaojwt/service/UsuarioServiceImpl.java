package org.spring.autenticacaojwt.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.spring.autenticacaojwt.dto.EnderecoDTO;
import org.spring.autenticacaojwt.dto.SenhaDTO;
import org.spring.autenticacaojwt.dto.UsuarioDTO;
import org.spring.autenticacaojwt.enums.PerfilUsuario;
import org.spring.autenticacaojwt.excecoes.lancaveis.DeletarEntidadeException;
import org.spring.autenticacaojwt.excecoes.lancaveis.EntidadeNaoEncontradaException;
import org.spring.autenticacaojwt.model.Endereco;
import org.spring.autenticacaojwt.model.Usuario;
import org.spring.autenticacaojwt.repository.UsuarioRepository;
import org.spring.autenticacaojwt.seguranca.UsuarioDetails;
import org.spring.autenticacaojwt.service.interfaces.EnderecoService;
import org.spring.autenticacaojwt.service.interfaces.UsuarioService;
import org.spring.autenticacaojwt.util.ConversorEntidadeDTOUtil;
import org.spring.autenticacaojwt.util.VerificadorUsuarioUtil;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;
import static org.spring.autenticacaojwt.util.ConstantesUtil.PROPRIEDADES_ADMIN;
import static org.spring.autenticacaojwt.util.ConversorEntidadeDTOUtil.converterParaDTO;
import static org.springframework.beans.BeanUtils.copyProperties;


@Slf4j(topic = "USUARIO_SERVICE")
@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final EnderecoService enderecoService;
    private final PasswordEncoderImpl encriptadorSenha;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, EnderecoService enderecoService) {
        this.usuarioRepository = usuarioRepository;
        this.enderecoService = enderecoService;
        this.encriptadorSenha = PasswordEncoderImpl.getInstancia();
    }

    /**
     * Encontra um usuário a partir do seu id
     *
     * @param id id do usuário
     * @return usuário encontrado
     */
    @Override
    public UsuarioDTO encontrarPorId(@NotNull UUID id) {
        log.info(">>> encontrarPorId: encontrando usuário por id");
        VerificadorUsuarioUtil.verificarAutorizacao(id);
        return usuarioRepository.findById(id)
                .map(ConversorEntidadeDTOUtil::converterParaDTO)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(String.format("usuário não encontrado, id: %s", id)));
    }

    /**
     * Encontra um usuário a partir do seu nome
     *
     * @param email email do usuário
     * @return usuário encontrado
     */
    @Override
    public Usuario encontrarUsuarioPorEmail(@NotNull String email) {
        log.info(">>> encontrarUsuarioPorEmail: encontrando usuário por email");
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (isNull(usuario))
            throw new EntidadeNaoEncontradaException(String.format("usuário não encontrado, email: %s", email));
        return usuario;
    }

    /**
     * Lista todos os usuários criados
     *
     * @return lista de usuários
     */
    @Override
    public List<UsuarioDTO> listarTodos() {
        VerificadorUsuarioUtil.verificarAutorizacao();
        log.info(">>> listarTodos: listando todos usuários");
        return usuarioRepository.findAll()
                .stream()
                .map(ConversorEntidadeDTOUtil::converterParaDTO).toList();
    }

    /**
     * Cria um novo usuário
     *
     * @param usuario objeto do tipo Usuario
     * @return novo usuário criado
     */
    @Override
    @Transactional
    public UsuarioDTO criar(@NotNull Usuario usuario) {
        log.info(">>> criar: criando usuário");
        usuario.setId(null);
        usuario.setDataCriacao(LocalDateTime.now());
        usuario.setDataUltimaModificacao(LocalDateTime.now());
        usuario.setSenha(encriptadorSenha.encode(usuario.getSenha()));
        usuario.setPerfilUsuario(PerfilUsuario.USUARIO.getCodigo());
        usuario = usuarioRepository.save(usuario);
        log.info(String.format(">>> criar: usuário criado, id: %s", usuario.getId()));
        return converterParaDTO(usuario);
    }

    /**
     * Atualiza um usuário previamente cadastrado
     *
     * @param usuario objeto do tipo Usuario (informações atualizadas)
     * @return novo usuário atualizado
     */
    @Override
    public UsuarioDTO atualizar(@NotNull Usuario usuario) {
        UsuarioDetails usuarioDetails = VerificadorUsuarioUtil.verificarAutorizacao(usuario.getId());
        log.info(">>> atualizar: atualizando usuário");
        UsuarioDTO usuarioDTO = encontrarPorId(usuario.getId());
        EnderecoDTO enderecoDTO = enderecoService.encontrarPorId(usuarioDTO.idEndereco());
        Usuario usuarioAtualizado = new Usuario();
        Endereco enderecoAtualizado = new Endereco();
        copyProperties(usuarioDTO, usuarioAtualizado);
        copyProperties(enderecoDTO, enderecoAtualizado);
        atualizarEntidade(usuario, usuarioAtualizado, PROPRIEDADES_ADMIN);
        atualizarEntidade(usuario.getEndereco(), enderecoAtualizado, PROPRIEDADES_ADMIN);
        usuarioAtualizado.setDataUltimaModificacao(LocalDateTime.now());
        usuarioAtualizado.setSenha(usuarioRepository.encontrarSenhaUsuarioPorId(usuarioDTO.id()));
        usuarioAtualizado.setEndereco(enderecoAtualizado);
        if (usuarioDetails.ehPerfil(PerfilUsuario.ADMIN))
            usuarioAtualizado.setPerfilUsuario(usuario.getPerfilUsuario());
        enderecoAtualizado.setUsuario(usuarioAtualizado);
        usuarioRepository.save(usuarioAtualizado);
        log.info(String.format(">>> atualizar: usuário atualizado, id: %s", usuarioAtualizado.getId()));
        return usuarioDTO;
    }

    /**
     * Delete um usuário a partir do seu id
     *
     * @param id id do usuário
     */
    @Override
    public void deletar(@NotNull UUID id) {
        VerificadorUsuarioUtil.verificarAutorizacao(id);
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
        VerificadorUsuarioUtil.verificarAutorizacao(id);
        UsuarioDTO usuarioDTO = encontrarPorId(id);
        if (encriptadorSenha.matches(senhaDTO.senhaOriginal(), usuarioRepository.encontrarSenhaUsuarioPorId(usuarioDTO.id())))
            usuarioRepository.atualizarSenhaUsuario(encriptadorSenha.encode(senhaDTO.senhaAtualizada()), id);
        else
            throw new DataIntegrityViolationException(String.format("senha original incorreta, id do usuário: %s", id));
    }

    /**
     * Encontra um usuário a partir do id do seu endereço
     *
     * @param id id do endereço do usuário
     * @return usuário encontrado
     */
    protected Usuario encontrarUsuarioPorIdEndereco(@NotNull UUID id) {
        log.info(">>> encontrarUsuarioPorIdEndereco: encontrando usuário por endereco");
        Usuario usuario = usuarioRepository.encontrarUsuarioPorIdEndereco(id);
        if (isNull(usuario))
            throw new EntidadeNaoEncontradaException(String.format("usuário não encontrado, id: %s", id));
        return usuario;
    }

    /**
     * Atualiza os atributos de um objeto atualizado em relação a um objeto original
     *
     * @param objOriginal   objeto original
     * @param objAtualizado objeto atualizado
     */
    private void atualizarEntidade(Object objOriginal, Object objAtualizado, List<String> propIgnoradas) {

        final BeanWrapper bwObjOriginal = new BeanWrapperImpl(objOriginal);
        final BeanWrapper bwObjAtualizado = new BeanWrapperImpl(objAtualizado);

        if (!Arrays.equals(bwObjAtualizado.getPropertyDescriptors(), bwObjOriginal.getPropertyDescriptors())) {
            log.info(">>> atualizarEntidade: objAtualizado não é do mesmo tipo do objOriginal");
            return;
        }

        PropertyDescriptor[] propriedades = bwObjAtualizado.getPropertyDescriptors();

        for (PropertyDescriptor prop : propriedades) {
            Object propObjOriginal = bwObjOriginal.getPropertyValue(prop.getName());
            Object propObjAtualizado = bwObjAtualizado.getPropertyValue(prop.getName());

            if (propObjOriginal != null && propObjAtualizado != null && !propObjAtualizado.equals(propObjOriginal) && !propIgnoradas.contains(prop.getName()))
                bwObjAtualizado.setPropertyValue(prop.getName(), propObjOriginal);
        }
    }
}
