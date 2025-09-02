package com.pthiago.gcp.api.infrastructure.persistence.jpa;

import com.pthiago.gcp.api.domain.model.NotaFiscal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotaFiscalJpaRepository extends JpaRepository<NotaFiscal, Long> {
    List<NotaFiscal> findByFornecedorId(Long fornecedorId);
}
