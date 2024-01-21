package org.spring.sistemaodontologia.repository;

import org.spring.sistemaodontologia.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, UUID> {

    /**
     * Busca o endereço do usuário
     *
     * @param id id do usuário
     * @return endereço buscado
     */
    @Query("SELECT u.endereco FROM Usuario u WHERE  u.id = :id")
    Endereco buscarEnderecoPorIdUsuario(UUID id);
}
