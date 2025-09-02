package com.pthiago.gcp.api.infrastructure.web.rest.mapper;

import com.pthiago.gcp.api.domain.dto.FornecedorRequestDTO;
import com.pthiago.gcp.api.domain.dto.FornecedorResponseDTO;
import com.pthiago.gcp.api.domain.model.Fornecedor;

import java.util.Collections;
import java.util.List;

/**
 * Classe utilitária para mapear entre a entidade Fornecedor e seus DTOs.
 * Isola a lógica de conversão, mantendo os serviços e controllers limpos.
 */
public class FornecedorMapper {

    /**
     * Converte uma Entidade Fornecedor para um FornecedorResponseDTO.
     * Usado para preparar os dados que serão enviados na resposta da API.
     *
     * @param fornecedor A entidade a ser convertida.
     * @return O DTO correspondente.
     */
    public static FornecedorResponseDTO toResponseDTO(Fornecedor fornecedor) {
        if (fornecedor == null) {
            return null;
        }
        return new FornecedorResponseDTO(
                fornecedor.getId(),
                fornecedor.getNome(),
                fornecedor.getCnpj(),
                fornecedor.getTelefone(),
                fornecedor.getEmail()
        );
    }

    /**
     * Converte um FornecedorRequestDTO para uma Entidade Fornecedor.
     * Usado para transformar os dados recebidos na requisição da API em uma entidade
     * que pode ser salva no banco de dados.
     *
     * @param requestDTO O DTO a ser convertido.
     * @return A entidade correspondente.
     */
    public static Fornecedor toEntity(FornecedorRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome(requestDTO.nome());
        fornecedor.setCnpj(requestDTO.cnpj());
        fornecedor.setTelefone(requestDTO.telefone());
        fornecedor.setEmail(requestDTO.email());
        return fornecedor;
    }

    /**
     * Converte uma lista de Entidades Fornecedor para uma lista de FornecedorResponseDTO.
     *
     * @param fornecedores A lista de entidades a ser convertida.
     * @return A lista de DTOs correspondente ou uma lista vazia se a entrada for nula.
     */
    public static List<FornecedorResponseDTO> toResponseDTOList(List<Fornecedor> fornecedores) {
        if (fornecedores == null || fornecedores.isEmpty()) {
            return Collections.emptyList();
        }
        return fornecedores.stream()
                .map(FornecedorMapper::toResponseDTO)
                .toList();
    }
}
