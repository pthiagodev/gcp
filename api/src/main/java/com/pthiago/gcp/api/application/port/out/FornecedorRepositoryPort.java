package com.pthiago.gcp.api.application.port.out;

import com.pthiago.gcp.api.domain.model.Fornecedor;

import java.util.List;
import java.util.Optional;

public interface FornecedorRepositoryPort {
    Fornecedor salvar(Fornecedor fornecedor);

    Optional<Fornecedor> buscarPeloId(Long fornecedorId);

    List<Fornecedor> listar();

    void deletar(Long fornecedorId);

    boolean existePorId(Long fornecedorId);

}
