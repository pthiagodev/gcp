package com.pthiago.gcp.api.domain.dto;

import com.pthiago.gcp.api.domain.model.Fornecedor;

public record FornecedorResponseDTO(
        Long id,
        String nome,
        String cnpj,
        String telefone,
        String email
) {
    /**
     * Método de fábrica para converter uma entidade Fornecedor em um DTO de resposta.
     * @param fornecedor A entidade do domínio.
     * @return O DTO correspondente.
     */
    public static FornecedorResponseDTO fromEntity(Fornecedor fornecedor) {
        return new FornecedorResponseDTO(
                fornecedor.getId(),
                fornecedor.getNome(),
                fornecedor.getCnpj(),
                fornecedor.getTelefone(),
                fornecedor.getEmail()
        );
    }
}
