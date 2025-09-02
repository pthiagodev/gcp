package com.pthiago.gcp.api.infrastructure.persistence.adapter;

import com.pthiago.gcp.api.application.port.out.NotaFiscalRepositoryPort;
import com.pthiago.gcp.api.domain.model.NotaFiscal;
import com.pthiago.gcp.api.infrastructure.persistence.jpa.NotaFiscalJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class NotaFiscalRepositoryAdapter implements NotaFiscalRepositoryPort {

    private final NotaFiscalJpaRepository jpaRepository;

    public NotaFiscalRepositoryAdapter(NotaFiscalJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public NotaFiscal salvar(NotaFiscal notaFiscal) {
        return jpaRepository.save(notaFiscal);
    }
}
