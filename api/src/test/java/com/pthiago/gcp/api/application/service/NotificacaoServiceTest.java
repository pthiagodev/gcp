package com.pthiago.gcp.api.application.service;

import com.pthiago.gcp.api.application.port.out.EmailSenderPort;
import com.pthiago.gcp.api.domain.exception.NotificationFailureException;
import com.pthiago.gcp.api.domain.model.Fornecedor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("🧪 Testes do Serviço de Notificação")
class NotificacaoServiceTest {

    @Mock
    private EmailSenderPort emailSenderPort;

    @InjectMocks
    private NotificacaoService notificacaoService;

    @Nested
    @DisplayName("Quando notificar um fornecedor sobre uma nota fiscal")
    class NotificarFornecedor {

        @Test
        @DisplayName("✅ Deve preparar e enviar a notificação com sucesso")
        void deveEnviarNotificacaoComSucesso() throws IOException {
            Fornecedor fornecedor = new Fornecedor();
            fornecedor.setNome("Fornecedor Exemplo");
            fornecedor.setEmail("contato@fornecedor.com");
            String numeroNF = "987654";
            File anexo = File.createTempFile("test", ".pdf");
            anexo.deleteOnExit();

            notificacaoService.notificarFornecedorSobreNotaFiscal(fornecedor, numeroNF, anexo);

            ArgumentCaptor<String> paraCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<String> assuntoCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<String> corpoCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<File> anexoCaptor = ArgumentCaptor.forClass(File.class);

            verify(emailSenderPort).enviarEmailComAnexo(paraCaptor.capture(), assuntoCaptor.capture(), corpoCaptor.capture(), anexoCaptor.capture());

            assertThat(paraCaptor.getValue()).isEqualTo("contato@fornecedor.com");
            assertThat(assuntoCaptor.getValue()).contains("Nota Fiscal 987654", "Fornecedor Exemplo");
        }

        @Test
        @DisplayName("❌ Deve lançar NotificationFailureException se a porta de envio de e-mail falhar")
        void deveLancarExcecaoSePortaDeEmailFalhar() throws IOException {
            Fornecedor fornecedor = new Fornecedor();
            File anexo = File.createTempFile("test", ".pdf");
            anexo.deleteOnExit();

            doThrow(new RuntimeException("SMTP server offline")).when(emailSenderPort).enviarEmailComAnexo(anyString(), anyString(), anyString(), any(File.class));

            assertThatThrownBy(() -> notificacaoService.notificarFornecedorSobreNotaFiscal(fornecedor, "123", anexo))
                    .isInstanceOf(NotificationFailureException.class)
                    .hasMessageContaining("Falha ao enviar e-mail de notificação.");
        }
    }
}

