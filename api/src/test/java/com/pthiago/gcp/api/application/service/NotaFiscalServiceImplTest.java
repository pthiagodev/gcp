package com.pthiago.gcp.api.application.service;

import com.pthiago.gcp.api.application.port.out.*;
import com.pthiago.gcp.api.domain.dto.ArquivoInfoDTO;
import com.pthiago.gcp.api.domain.model.Fornecedor;
import com.pthiago.gcp.api.domain.model.NotaFiscal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

// Import estático do AssertJ
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("🧪 Testes do Serviço de Orquestração de Notas Fiscais")
class NotaFiscalServiceImplTest {

    @Mock private FornecedorRepositoryPort fornecedorRepositoryPort;
    @Mock private NotaFiscalRepositoryPort notaFiscalRepositoryPort;
    @Mock private FileStoragePort fileStoragePort;
    @Mock private NotificacaoServiceImpl notificacaoService;

    @InjectMocks
    private NotaFiscalServiceImpl notaFiscalService;

    @Nested
    @DisplayName("Cenários de Sucesso")
    class CenariosDeSucesso {
        @Test
        @DisplayName("✅ Deve orquestrar com sucesso o salvamento e envio da nota")
        void deveOrquestrarSalvarEEnviarNotaComSucesso() {
            Long fornecedorId = 1L;
            String numeroNF = "12345";
            ArquivoInfoDTO arquivoInfo = new ArquivoInfoDTO("original.pdf", InputStream.nullInputStream());

            Fornecedor fornecedorMock = new Fornecedor();
            fornecedorMock.setId(fornecedorId);
            fornecedorMock.setNome("Fornecedor Mock");

            Path caminhoMock = Paths.get("/tmp/Fornecedor_Mock-12345.pdf");

            when(fornecedorRepositoryPort.buscarPeloId(fornecedorId)).thenReturn(Optional.of(fornecedorMock));
            when(fileStoragePort.salvar(anyString(), any(ArquivoInfoDTO.class))).thenReturn(caminhoMock);
            when(notaFiscalRepositoryPort.salvar(any(NotaFiscal.class))).thenAnswer(invocation -> invocation.getArgument(0));
            doNothing().when(notificacaoService).notificarFornecedorSobreNotaFiscal(any(), anyString(), any());

            notaFiscalService.salvarEEnviarNotaFiscal(fornecedorId, numeroNF, arquivoInfo);

            verify(fornecedorRepositoryPort, times(1)).buscarPeloId(fornecedorId);
            verify(fileStoragePort, times(1)).salvar(anyString(), eq(arquivoInfo));
            verify(notaFiscalRepositoryPort, times(1)).salvar(any(NotaFiscal.class));
            verify(notificacaoService, times(1)).notificarFornecedorSobreNotaFiscal(eq(fornecedorMock), eq(numeroNF), any());
        }
    }

    @Nested
    @DisplayName("Cenários de Falha")
    class CenariosDeFalha {
        @Test
        @DisplayName("❌ Deve lançar uma exceção se o fornecedor não for encontrado")
        void deveLancarExcecaoQuandoFornecedorNaoForEncontrado() {
            Long fornecedorIdInexistente = 99L;
            String numeroNF = "12345";
            ArquivoInfoDTO arquivoInfo = new ArquivoInfoDTO("original.pdf", InputStream.nullInputStream());

            when(fornecedorRepositoryPort.buscarPeloId(fornecedorIdInexistente)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> {
                notaFiscalService.salvarEEnviarNotaFiscal(fornecedorIdInexistente, numeroNF, arquivoInfo);
            })
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("não encontrado");

            verify(fileStoragePort, never()).salvar(any(), any());
            verify(notaFiscalRepositoryPort, never()).salvar(any());
            verify(notificacaoService, never()).notificarFornecedorSobreNotaFiscal(any(), any(), any());
        }
    }
}