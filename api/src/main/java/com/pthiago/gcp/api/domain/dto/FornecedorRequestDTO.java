package com.pthiago.gcp.api.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CNPJ;

public record FornecedorRequestDTO(
    @NotBlank(message = "O nome não pode estar em branco.")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres.")
    String nome,

    @NotBlank(message = "O CNPJ não pode estar em branco.")
    @CNPJ
    String cnpj,

    @NotBlank(message = "O telefone não pode estar em branco.")
    @Pattern(regexp = "^\\(\\d{2}\\)\\s?\\d{4,5}-\\d{4}$", message = "O telefone deve estar no formato (XX) XXXXX-XXXX.")
    String telefone,

    @NotBlank(message = "O e-mail não pode estar em branco.")
    @Email(message = "O formato do e-mail é inválido.")
    String email
) {}
