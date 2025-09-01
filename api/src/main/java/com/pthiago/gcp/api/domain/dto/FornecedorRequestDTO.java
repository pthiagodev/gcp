package com.pthiago.gcp.api.domain.dto;

public record FornecedorRequestDTO(
    String nome,
    String cnpj,
    String telefone,
    String email
) {}
