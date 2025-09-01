package com.pthiago.gcp.api.application.service;

import com.pthiago.gcp.api.application.port.in.FornecedorUseCase;
import com.pthiago.gcp.api.application.port.out.FornecedorRepositoryPort;
import com.pthiago.gcp.api.domain.dto.FornecedorRequestDTO;
import com.pthiago.gcp.api.domain.model.Fornecedor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FornecedorServiceImpl implements FornecedorUseCase {

    private static final Logger log = LoggerFactory.getLogger(FornecedorServiceImpl.class);
    private final FornecedorRepositoryPort fornecedorRepositoryPort;

    public FornecedorServiceImpl(FornecedorRepositoryPort fornecedorRepositoryPort) {
        this.fornecedorRepositoryPort = fornecedorRepositoryPort;
    }

    @Transactional
    @Override
    public Fornecedor criarFornecedor(FornecedorRequestDTO requestDTO) {
        log.info("Iniciando criação de novo fornecedor com CNPJ: {}", requestDTO.cnpj());
        Fornecedor novoFornecedor = new Fornecedor();
        novoFornecedor.setNome(requestDTO.nome());
        novoFornecedor.setCnpj(requestDTO.cnpj());
        novoFornecedor.setEmail(requestDTO.email());
        Fornecedor fornecedorSalvo = fornecedorRepositoryPort.salvar(novoFornecedor);
        log.info("Fornecedor criado com sucesso. ID: {}", fornecedorSalvo.getId());
        return fornecedorSalvo;
    }

    @Override
    public Optional<Fornecedor> buscarFornecedorPorId(Long id) {
        log.info("Buscando fornecedor com ID: {}", id);
        return fornecedorRepositoryPort.buscarPeloId(id);
    }

    @Override
    public List<Fornecedor> buscarTodosFornecedores() {
        log.info("Buscando todos os fornecedores");
        return fornecedorRepositoryPort.listar();
    }

    @Transactional
    @Override
    public Optional<Fornecedor> atualizarFornecedor(Long id, FornecedorRequestDTO requestDTO) {
        log.info("Iniciando atualização do fornecedor com ID: {}", id);
        return fornecedorRepositoryPort.buscarPeloId(id).map(fornecedorExistente -> {
            fornecedorExistente.setNome(requestDTO.nome());
            fornecedorExistente.setCnpj(requestDTO.cnpj());
            fornecedorExistente.setTelefone(requestDTO.telefone());
            fornecedorExistente.setEmail(requestDTO.email());
            Fornecedor fornecedorAtualizado = fornecedorRepositoryPort.salvar(fornecedorExistente);
            log.info("Fornecedor com ID: {} atualizado com sucesso.", id);
            return fornecedorAtualizado;
        });
    }

    @Transactional
    @Override
    public boolean deletarFornecedor(Long id) {
        log.info("Iniciando exclusão do fornecedor com ID: {}", id);
        if (fornecedorRepositoryPort.existePorId(id)) {
            fornecedorRepositoryPort.deletar(id);
            log.info("Fornecedor com ID: {} excluído com sucesso.", id);
            return true;
        }
        log.warn("Tentativa de exclusão de fornecedor com ID: {} não encontrado.", id);
        return false;
    }

}
