package com.pthiago.gcp.api.infrastructure.persistence.adapter;

import com.pthiago.gcp.api.application.port.out.FornecedorRepositoryPort;
import com.pthiago.gcp.api.domain.model.Fornecedor;
import com.pthiago.gcp.api.infrastructure.persistence.jpa.FornecedorJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class FornecedorRepositoryAdapter implements FornecedorRepositoryPort {

    private final FornecedorJpaRepository jpaRepository;

    public FornecedorRepositoryAdapter(FornecedorJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Fornecedor salvar(Fornecedor fornecedor) {
        return jpaRepository.save(fornecedor);
    }

    @Override
    public Optional<Fornecedor> buscarPeloId(Long fornecedorId) {
        return jpaRepository.findById(fornecedorId);
    }

    @Override
    public List<Fornecedor> listar() {
        return jpaRepository.findAll();
    }

    @Override
    public void deletar(Long fornecedorId) {
        jpaRepository.deleteById(fornecedorId);
    }

    @Override
    public boolean existePorId(Long fornecedorId) {
        return jpaRepository.existsById(fornecedorId);
    }
}
