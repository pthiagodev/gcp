package com.pthiago.gcp.api.application.service;

import com.pthiago.gcp.api.application.port.out.EmailSenderPort;
import com.pthiago.gcp.api.domain.model.Fornecedor;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class NotificacaoServiceImpl {

    private final EmailSenderPort emailSenderPort;

    public NotificacaoServiceImpl(EmailSenderPort emailSenderPort) {
        this.emailSenderPort = emailSenderPort;
    }

    /**
     * Prepara e envia a notificação por e-mail para o fornecedor.
     * @param fornecedor O objeto do fornecedor que será notificado.
     * @param numeroNF O número da nota fiscal recebido do frontend.
     * @param anexo O arquivo PDF da nota fiscal a ser anexado.
     */
    public void notificarFornecedorSobreNotaFiscal(Fornecedor fornecedor, String numeroNF, File anexo) {
        String assunto = "Nota Fiscal " + numeroNF + " - " + fornecedor.getNome();
        String corpo = "Olá,\n\nSegue em anexo a nota fiscal " + numeroNF + " referente ao pagamento recente.\n\nAtenciosamente,\nFinanceiro.";

        emailSenderPort.enviarEmailComAnexo(fornecedor.getEmail(), assunto, corpo, anexo);
    }

}
