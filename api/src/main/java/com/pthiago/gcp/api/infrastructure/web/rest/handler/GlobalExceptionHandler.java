package com.pthiago.gcp.api.infrastructure.web.rest.handler;

import com.pthiago.gcp.api.domain.dto.ErrorResponseDTO;
import com.pthiago.gcp.api.domain.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Captura exceções de validação do @Valid.
     * Quando um DTO de requisição falha na validação, esta exceção é lançada.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
                ));

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Erro de validação nos dados de entrada.",
                errors
        );
        log.warn("Erro de validação detectado: {}", errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Captura exceção customizada para recursos não encontrados.
     * Retorna um status 404 Not Found.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                null
        );
        log.warn("Recurso não encontrado: {}", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Captura qualquer outra exceção não tratada (o "catch-all").
     * Garante que a API sempre retorne um JSON padronizado para erros inesperados.
     * Retorna um status 500 Internal Server Error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Ocorreu um erro inesperado no servidor.",
                null
        );
        log.error("Erro interno não tratado:", ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
