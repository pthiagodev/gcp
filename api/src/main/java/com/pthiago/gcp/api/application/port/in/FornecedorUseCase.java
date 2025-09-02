package com.pthiago.gcp.api.application.port.in;

import com.pthiago.gcp.api.domain.dto.FornecedorRequestDTO;
import com.pthiago.gcp.api.domain.dto.FornecedorResponseDTO;
import com.pthiago.gcp.api.domain.exception.ResourceNotFoundException;

import java.util.List;

/**
 * Contrato (Porta de Entrada) para os casos de uso relacionados à gestão de Fornecedores.
 * Define todas as operações de negócio que a aplicação expõe para a camada externa (ex: Controllers).
 */
public interface FornecedorUseCase {

    /**
     * Cria um novo fornecedor com base nos dados fornecidos.
     *
     * @param requestDTO DTO contendo os dados do fornecedor a ser criado.
     * @return DTO com os dados do fornecedor recém-criado, incluindo seu ID.
     */
    FornecedorResponseDTO criarFornecedor(FornecedorRequestDTO requestDTO);

    /**
     * Busca um fornecedor específico pelo seu ID.
     *
     * @param id O ID do fornecedor a ser buscado.
     * @return DTO com os dados do fornecedor encontrado.
     * @throws ResourceNotFoundException se nenhum fornecedor for encontrado com o ID fornecido.
     */
    FornecedorResponseDTO buscarFornecedorPorId(Long id);

    /**
     * Retorna uma lista com todos os fornecedores cadastrados.
     *
     * @return Uma lista de DTOs, cada um representando um fornecedor. A lista pode ser vazia.
     */
    List<FornecedorResponseDTO> buscarTodosFornecedores();

    /**
     * Atualiza os dados de um fornecedor existente.
     *
     * @param id O ID do fornecedor a ser atualizado.
     * @param requestDTO DTO contendo os novos dados para o fornecedor.
     * @return DTO com os dados do fornecedor após a atualização.
     * @throws ResourceNotFoundException se nenhum fornecedor for encontrado com o ID fornecido.
     */
    FornecedorResponseDTO atualizarFornecedor(Long id, FornecedorRequestDTO requestDTO);

    /**
     * Exclui um fornecedor do sistema pelo seu ID.
     *
     * @param id O ID do fornecedor a ser excluído.
     * @throws ResourceNotFoundException se nenhum fornecedor for encontrado com o ID fornecido.
     */
    void deletarFornecedor(Long id);
}