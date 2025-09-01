package com.pthiago.gcp.api.application.port.in;

import com.pthiago.gcp.api.domain.dto.FornecedorRequestDTO;
import com.pthiago.gcp.api.domain.model.Fornecedor;

import java.util.List;
import java.util.Optional;

public interface FornecedorUseCase {
    Fornecedor criarFornecedor(FornecedorRequestDTO requestDTO);

    Optional<Fornecedor> buscarFornecedorPorId(Long id);

    List<Fornecedor> buscarTodosFornecedores();

    Optional<Fornecedor> atualizarFornecedor(Long id, FornecedorRequestDTO requestDTO);

    boolean deletarFornecedor(Long id);
}
