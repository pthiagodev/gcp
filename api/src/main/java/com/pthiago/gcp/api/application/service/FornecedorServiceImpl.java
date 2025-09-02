package com.pthiago.gcp.api.application.service;

import com.pthiago.gcp.api.application.port.in.FornecedorUseCase;
import com.pthiago.gcp.api.application.port.out.FornecedorRepositoryPort;
import com.pthiago.gcp.api.domain.dto.FornecedorRequestDTO;
import com.pthiago.gcp.api.domain.dto.FornecedorResponseDTO;
import com.pthiago.gcp.api.domain.exception.ResourceNotFoundException;
import com.pthiago.gcp.api.domain.model.Fornecedor;
import com.pthiago.gcp.api.infrastructure.web.rest.mapper.FornecedorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FornecedorServiceImpl implements FornecedorUseCase {

    private static final Logger log = LoggerFactory.getLogger(FornecedorServiceImpl.class);
    private final FornecedorRepositoryPort fornecedorRepositoryPort;

    public FornecedorServiceImpl(FornecedorRepositoryPort fornecedorRepositoryPort) {
        this.fornecedorRepositoryPort = fornecedorRepositoryPort;
    }

    @Transactional
    @Override
    public FornecedorResponseDTO criarFornecedor(FornecedorRequestDTO requestDTO) {
        log.info("Iniciando criação de novo fornecedor com CNPJ: {}", requestDTO.cnpj());
        var fornecedor = FornecedorMapper.toEntity(requestDTO);
        var salvo = fornecedorRepositoryPort.salvar(fornecedor);
        log.info("Fornecedor '{}' salvo com sucesso. ID: {}", salvo.getNome(), salvo.getId());
        return FornecedorMapper.toResponseDTO(salvo);
    }

    @Override
    public FornecedorResponseDTO buscarFornecedorPorId(Long id) {
        log.info("Buscando fornecedor com ID: {}", id);
        var fornecedor = buscarPeloId(id);
        return FornecedorMapper.toResponseDTO(fornecedor);
    }

    @Override
    public List<FornecedorResponseDTO> buscarTodosFornecedores() {
        log.info("Buscando todos os fornecedores.");
        var fornecedores = fornecedorRepositoryPort.listar();
        log.info("Encontrados {} fornecedores.", fornecedores.size());
        return FornecedorMapper.toResponseDTOList(fornecedores);
    }

    @Transactional
    @Override
    public FornecedorResponseDTO atualizarFornecedor(Long id, FornecedorRequestDTO requestDTO) {
        log.info("Iniciando processo de atualização para o fornecedor ID: {}", id);
        var fornecedorExistente = buscarPeloId(id);

        fornecedorExistente.setNome(requestDTO.nome());
        fornecedorExistente.setCnpj(requestDTO.cnpj());
        fornecedorExistente.setTelefone(requestDTO.telefone());
        fornecedorExistente.setEmail(requestDTO.email());

        var atualizado = fornecedorRepositoryPort.salvar(fornecedorExistente);
        log.info("Fornecedor ID {} atualizado com sucesso.", id);
        return FornecedorMapper.toResponseDTO(atualizado);
    }

    @Transactional
    @Override
    public void deletarFornecedor(Long id) {
        log.info("Iniciando processo de exclusão para o fornecedor ID: {}", id);
        if (!fornecedorRepositoryPort.existePorId(id)) {
            log.warn("Tentativa de excluir fornecedor não existente com ID: {}", id);
            throw new ResourceNotFoundException("Fornecedor com ID " + id + " não encontrado para exclusão.");
        }
        fornecedorRepositoryPort.deletar(id);
        log.info("Fornecedor ID {} excluído com sucesso.", id);
    }

    private Fornecedor buscarPeloId(Long id) {
        return fornecedorRepositoryPort.buscarPeloId(id)
                .orElseThrow(() -> {
                    log.warn("Fornecedor com ID: {} não encontrado.", id);
                    return new ResourceNotFoundException("Fornecedor com ID " + id + " não encontrado.");
                });
    }

}
