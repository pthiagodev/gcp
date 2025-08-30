package com.pthiago.gcp.api.infrastructure.email;

import com.pthiago.gcp.api.application.port.out.EmailSenderPort;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class SmtpEmailSenderAdapter implements EmailSenderPort {

    private final JavaMailSender mailSender;

    public SmtpEmailSenderAdapter(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void enviarEmailComAnexo(String para, String assunto, String corpo, File anexo) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(para);
            helper.setSubject(assunto);
            helper.setText(corpo);
            helper.addAttachment(anexo.getName(), anexo);

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException("Falha ao enviar e-mail.", e);
        }
    }
}
