package com.pthiago.gcp.api.application.service;

import com.pthiago.gcp.api.application.port.out.EmailSenderPort;
import com.pthiago.gcp.api.domain.exception.NotificationFailureException;
import com.pthiago.gcp.api.domain.model.Fornecedor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class NotificacaoService {

    private static final Logger log = LoggerFactory.getLogger(NotificacaoService.class);

    private final EmailSenderPort emailSenderPort;

    public NotificacaoService(EmailSenderPort emailSenderPort) {
        this.emailSenderPort = emailSenderPort;
    }

    public void notificarFornecedorSobreNotaFiscal(Fornecedor fornecedor, String numeroNF, File anexo) {
        log.info("Iniciando preparação da notificação para o fornecedor: '{}' (E-mail: {}), NF: {}",
                fornecedor.getNome(), fornecedor.getEmail(), numeroNF);

        String assunto = "Nota Fiscal " + numeroNF + " - " + fornecedor.getNome();
        String corpo = "Olá,\n\nSegue em anexo a nota fiscal " + numeroNF + " referente ao pagamento recente.\n\nAtenciosamente,\nFinanceiro.";

        try {
            emailSenderPort.enviarEmailComAnexo(fornecedor.getEmail(), assunto, corpo, anexo);

            log.info("Notificação para o fornecedor '{}' enviada com sucesso.", fornecedor.getNome());

        } catch (Exception e) {
            log.error("Falha ao enviar e-mail de notificação para o fornecedor: '{}' (E-mail: {}). Erro: {}",
                    fornecedor.getNome(), fornecedor.getEmail(), e.getMessage(), e);

            throw new NotificationFailureException("Falha ao enviar e-mail de notificação.", e);
        }
    }
}
