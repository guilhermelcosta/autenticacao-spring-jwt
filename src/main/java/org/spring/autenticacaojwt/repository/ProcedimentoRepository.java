package org.spring.autenticacaojwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProcedimentoRepository extends JpaRepository<Procedimento, UUID> {
}
