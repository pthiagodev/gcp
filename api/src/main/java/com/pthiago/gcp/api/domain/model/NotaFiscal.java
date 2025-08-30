package com.pthiago.gcp.api.domain.model;

import java.time.LocalDateTime;

public class NotaFiscal {
    private Long id;
    private String nomeArquivo;
    private String caminhoArquivo;
    private LocalDateTime dataEnvio;
    private Fornecedor fornecedor;

    public NotaFiscal() {
    }

    public NotaFiscal(Long id, String nomeArquivo, String caminhoArquivo, LocalDateTime dataEnvio, Fornecedor fornecedor) {
        this.id = id;
        this.nomeArquivo = nomeArquivo;
        this.caminhoArquivo = caminhoArquivo;
        this.dataEnvio = dataEnvio;
        this.fornecedor = fornecedor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public String getCaminhoArquivo() {
        return caminhoArquivo;
    }

    public void setCaminhoArquivo(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    public LocalDateTime getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(LocalDateTime dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }
}
