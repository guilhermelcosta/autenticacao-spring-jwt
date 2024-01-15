package org.spring.autenticacaojwt.repository;

import org.spring.autenticacaojwt.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    /**
     * Encontra um usuário a partir do seu email
     *
     * @param email email do usuário
     * @return usuário encontrado
     */
    @Transactional(readOnly = true)
    Optional<Usuario> findByEmail(String email);

    /**
     * Encontra um usuário a partir do id do seu endereço
     *
     * @param id id do endereço do usuário
     * @return usuário encontrado
     */
    @Transactional(readOnly = true)
    @Query("""
            SELECT NEW Usuario(
            u.id,
            u.endereco,
            u.perfilUsuario,
            u.nome,
            u.rg,
            u.cpf,
            u.dataNascimento,
            u.email,
            u.senha,
            u.telefone,
            u.dataCriacao,
            u.dataUltimaModificacao,
            u.plano,
            u.consultas)
            FROM Usuario u
            WHERE u.endereco.id = :id
            """)
    Optional<Usuario> encontrarUsuarioPorIdEndereco(UUID id);

    /**
     * Encontra a senha de um usuário a partir do seu id
     * @param id id do usuário
     * @return senha do usuário
     */
    @Transactional(readOnly = true)
    @Query("SELECT u.senha FROM Usuario u WHERE u.id = :id")
    String encontrarSenhaUsuarioPorId(UUID id);

    /**
     * Atualiza a senha de um usuário
     *
     * @param senha senha atualizada
     * @param id    id do usuário
     */
    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.senha = :senha WHERE u.id = :id")
    void atualizarSenhaUsuario(String senha, UUID id);
}
