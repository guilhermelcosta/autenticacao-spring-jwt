package org.spring.sistemaodontologia.repository;

import org.spring.sistemaodontologia.model.Usuario;
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
     * Busca um usuário a partir do seu email
     *
     * @param email email do usuário
     * @return usuário buscado
     */
    @Transactional(readOnly = true)
    Optional<Usuario> findByEmail(String email);

    /**
     * Busca a senha de um usuário a partir do seu id
     * @param id id do usuário
     * @return senha do usuário
     */
    @Transactional(readOnly = true)
    @Query("SELECT u.senha FROM Usuario u WHERE u.id = :id")
    String buscarSenhaUsuarioPorId(UUID id);

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
