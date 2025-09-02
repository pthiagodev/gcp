package com.pthiago.gcp.api.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO para padronizar as respostas de erro da API.
 *
 * @param timestamp      Momento em que o erro ocorreu.
 * @param status         Código de status HTTP.
 * @param error          Descrição geral do erro (ex: Bad Request).
 * @param message        Mensagem principal para o usuário.
 * @param validationErrors Lista de erros de validação específicos (opcional).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponseDTO(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        Map<String, List<String>> validationErrors
) {}
