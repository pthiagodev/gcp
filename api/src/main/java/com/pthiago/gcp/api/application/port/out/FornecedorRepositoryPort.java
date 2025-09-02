package com.pthiago.gcp.api.application.port.out;

import com.pthiago.gcp.api.domain.model.Fornecedor;

import java.util.List;
import java.util.Optional;

public interface FornecedorRepositoryPort {
    Optional<Fornecedor> buscarPeloId(Long fornecedorId);
    List<Fornecedor> listar();
    Fornecedor salvar(Fornecedor fornecedor);
}
