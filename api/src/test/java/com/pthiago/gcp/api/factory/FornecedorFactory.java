package com.pthiago.gcp.api.factory;

import com.pthiago.gcp.api.domain.dto.FornecedorRequestDTO;
import com.pthiago.gcp.api.domain.model.Fornecedor;

/**
 * Classe utilitária para criar objetos de teste relacionados a Fornecedor.
 * Centraliza a criação de massa de dados para garantir consistência e reuso.
 */
public class FornecedorFactory {

    /**
     * Cria uma instância de FornecedorRequestDTO com um CNPJ fornecido.
     * @param cnpj O CNPJ a ser usado no DTO. Deve ser um valor único e válido
     * @return um DTO de requisição de fornecedor preenchido.
     */
    public static FornecedorRequestDTO criarFornecedorRequestDTO(String cnpj) {
        return new FornecedorRequestDTO(
                "Fornecedor de Fábrica",
                cnpj,
                "(85) 98765-4321",
                "fabrica@teste.com"
        );
    }

    /**
     * Cria uma instância da entidade Fornecedor com um CNPJ fornecido.
     * @param cnpj O CNPJ a ser usado na entidade. Deve ser um valor único, mas não precisa ser válido.
     * @return uma entidade Fornecedor preenchida.
     */
    public static Fornecedor criarFornecedor(String cnpj) {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("Fornecedor de Fábrica");
        fornecedor.setCnpj(cnpj);
        fornecedor.setTelefone("(85) 98765-4321");
        fornecedor.setEmail("fabrica@teste.com");
        return fornecedor;
    }
}

