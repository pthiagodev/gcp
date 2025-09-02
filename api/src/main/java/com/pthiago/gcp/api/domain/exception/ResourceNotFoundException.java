package com.pthiago.gcp.api.domain.exception;

/**
 * Exceção a ser lançada quando um recurso específico (ex: Fornecedor, NotaFiscal)
 * não é encontrado no sistema.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
