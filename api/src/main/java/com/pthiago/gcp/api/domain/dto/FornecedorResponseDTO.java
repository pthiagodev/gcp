package com.pthiago.gcp.api.domain.dto;

public record FornecedorResponseDTO(
        Long id,
        String nome,
        String cnpj,
        String telefone,
        String email
) {}
