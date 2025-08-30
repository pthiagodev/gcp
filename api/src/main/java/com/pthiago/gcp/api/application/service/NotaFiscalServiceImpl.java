package com.pthiago.gcp.api.application.service;

import com.pthiago.gcp.api.application.port.in.NotaFiscalUseCase;
import com.pthiago.gcp.api.application.port.out.*;
import com.pthiago.gcp.api.domain.dto.ArquivoInfoDTO;
import com.pthiago.gcp.api.domain.model.Fornecedor;
import com.pthiago.gcp.api.domain.model.NotaFiscal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.text.Normalizer;
import java.time.LocalDateTime;

@Service
public class NotaFiscalServiceImpl implements NotaFiscalUseCase {

    private final FornecedorRepositoryPort fornecedorRepositoryPort;
    private final NotaFiscalRepositoryPort notaFiscalRepositoryPort;
    private final FileStoragePort fileStoragePort;
    private final EmailSenderPort emailSenderPort;

    public NotaFiscalServiceImpl(FornecedorRepositoryPort fornecedorRepositoryPort, NotaFiscalRepositoryPort notaFiscalRepositoryPort, FileStoragePort fileStoragePort, EmailSenderPort emailSenderPort) {
        this.fornecedorRepositoryPort = fornecedorRepositoryPort;
        this.notaFiscalRepositoryPort = notaFiscalRepositoryPort;
        this.fileStoragePort = fileStoragePort;
        this.emailSenderPort = emailSenderPort;
    }

    @Transactional
    @Override
    public void salvarEEnviarNotaFiscal(Long fornecedorId, String numeroNF, ArquivoInfoDTO arquivoInfo) {
        Fornecedor fornecedor = fornecedorRepositoryPort.buscarPeloId(fornecedorId)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado!"));

        String novoNomeArquivo = criarNomeArquivo(fornecedor.getNome(), numeroNF);

        Path caminhoCompleto = fileStoragePort.salvar(novoNomeArquivo, arquivoInfo);

        NotaFiscal notaFiscal = new NotaFiscal();
        notaFiscal.setNomeArquivo(novoNomeArquivo);
        notaFiscal.setCaminhoArquivo(caminhoCompleto.toString());
        notaFiscal.setDataEnvio(LocalDateTime.now());
        notaFiscal.setFornecedor(fornecedor);
        notaFiscalRepositoryPort.salvar(notaFiscal);

        String assunto = "Nota Fiscal " + numeroNF + " - " + fornecedor.getNome();
        String corpo = "Olá,\n\nSegue em anexo a nota fiscal " + numeroNF + ".";
        emailSenderPort.enviarEmailComAnexo(fornecedor.getEmail(), assunto, corpo, caminhoCompleto.toFile());
    }

    private String criarNomeArquivo(String nomeFornecedor, String numeroNF) {
        String nomeNormalizado = Normalizer.normalize(nomeFornecedor, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("[^\\p{Alnum}]+", "_");
        return String.format("%s-%s.pdf", nomeNormalizado, numeroNF);
    }
}
