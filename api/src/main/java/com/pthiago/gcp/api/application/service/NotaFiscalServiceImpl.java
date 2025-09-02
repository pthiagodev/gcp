package com.pthiago.gcp.api.application.service;

import com.pthiago.gcp.api.application.port.in.NotaFiscalUseCase;
import com.pthiago.gcp.api.application.port.out.*;
import com.pthiago.gcp.api.domain.dto.ArquivoInfoDTO;
import com.pthiago.gcp.api.domain.model.Fornecedor;
import com.pthiago.gcp.api.domain.model.NotaFiscal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.text.Normalizer;
import java.time.LocalDateTime;

@Service
public class NotaFiscalServiceImpl implements NotaFiscalUseCase {

    private static final Logger log = LoggerFactory.getLogger(NotaFiscalServiceImpl.class);

    private final FornecedorRepositoryPort fornecedorRepositoryPort;
    private final NotaFiscalRepositoryPort notaFiscalRepositoryPort;
    private final FileStoragePort fileStoragePort;
    private final NotificacaoService notificacaoService;

    public NotaFiscalServiceImpl(FornecedorRepositoryPort fornecedorRepositoryPort, NotaFiscalRepositoryPort notaFiscalRepositoryPort, FileStoragePort fileStoragePort, NotificacaoService notificacaoService) {
        this.fornecedorRepositoryPort = fornecedorRepositoryPort;
        this.notaFiscalRepositoryPort = notaFiscalRepositoryPort;
        this.fileStoragePort = fileStoragePort;
        this.notificacaoService = notificacaoService;
    }

    @Transactional
    @Override
    public void salvarEEnviarNotaFiscal(Long fornecedorId, String numeroNF, ArquivoInfoDTO arquivoInfo) {
        log.info("Iniciando caso de uso: Salvar e Enviar Nota Fiscal. Fornecedor ID: {}, NF: '{}'", fornecedorId, numeroNF);

        Fornecedor fornecedor = buscarFornecedor(fornecedorId);

        String nomeArquivo = criarNomeArquivo(fornecedor.getNome(), numeroNF);
        Path caminhoArquivo = fileStoragePort.salvar(nomeArquivo, arquivoInfo);
        log.info("Arquivo da nota fiscal persistido em: {}", caminhoArquivo);

        NotaFiscal notaFiscal = registrarMetadadosNotaFiscal(fornecedor, nomeArquivo, caminhoArquivo.toString());
        log.info("Metadados da nota fiscal registrados no banco. ID da Nota: {}", notaFiscal.getId());

        notificacaoService.notificarFornecedorSobreNotaFiscal(fornecedor, numeroNF, caminhoArquivo.toFile());
        log.info("Notificação enviada para o fornecedor: {}", fornecedor.getEmail());
    }

    private Fornecedor buscarFornecedor(Long fornecedorId) {
        return fornecedorRepositoryPort.buscarPeloId(fornecedorId)
                .orElseThrow(() -> {
                    log.warn("Tentativa de envio para fornecedor não cadastrado. ID: {}", fornecedorId);
                    return new RuntimeException("Fornecedor com ID " + fornecedorId + " não encontrado."); // Idealmente uma exceção customizada
                });
    }

    private String criarNomeArquivo(String nomeFornecedor, String numeroNF) {
        String nomeNormalizado = Normalizer.normalize(nomeFornecedor, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("[^\\p{Alnum}]+", "_");
        return String.format("%s-%s.pdf", nomeNormalizado, numeroNF);
    }

    private NotaFiscal registrarMetadadosNotaFiscal(Fornecedor fornecedor, String nomeArquivo, String caminhoArquivo) {
        NotaFiscal notaFiscal = new NotaFiscal(
                null,
                nomeArquivo,
                caminhoArquivo,
                LocalDateTime.now(),
                fornecedor
        );

        return notaFiscalRepositoryPort.salvar(notaFiscal);
    }
}
