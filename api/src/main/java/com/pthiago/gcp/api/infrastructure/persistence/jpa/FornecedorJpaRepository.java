package com.pthiago.gcp.api.infrastructure.persistence.jpa;

import com.pthiago.gcp.api.domain.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FornecedorJpaRepository extends JpaRepository<Fornecedor, Long> {
    Optional<Fornecedor> findByCnpj(String cnpj);
}
