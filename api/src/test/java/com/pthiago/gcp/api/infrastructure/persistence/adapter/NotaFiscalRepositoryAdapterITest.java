package com.pthiago.gcp.api.infrastructure.persistence.adapter;

import com.pthiago.gcp.api.AbstractIntegrationTest;
import com.pthiago.gcp.api.domain.model.Fornecedor;
import com.pthiago.gcp.api.domain.model.NotaFiscal;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@DisplayName("🧪 Testes de Integração para o NotaFiscalRepositoryAdapter")
class NotaFiscalRepositoryAdapterITest extends AbstractIntegrationTest {

    @Autowired
    private NotaFiscalRepositoryAdapter notaFiscalRepositoryAdapter;

    @Autowired
    private FornecedorRepositoryAdapter fornecedorRepositoryAdapter;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("✅ Deve salvar uma nota fiscal com sucesso")
    void deveSalvarNotaFiscalComSucesso() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("Fornecedor para NF");
        fornecedor.setCnpj("99.999.999/0001-99");
        fornecedor.setEmail("nf@teste.com");
        Fornecedor fornecedorSalvo = fornecedorRepositoryAdapter.salvar(fornecedor);
        entityManager.flush();

        NotaFiscal novaNotaFiscal = new NotaFiscal();
        novaNotaFiscal.setFornecedor(fornecedorSalvo);
        novaNotaFiscal.setNomeArquivo("fornecedor-123.pdf");
        novaNotaFiscal.setCaminhoArquivo("/dados/2025/09/fornecedor-123.pdf");
        novaNotaFiscal.setDataEnvio(LocalDateTime.now());

        NotaFiscal notaFiscalSalva = notaFiscalRepositoryAdapter.salvar(novaNotaFiscal);

        assertThat(notaFiscalSalva).isNotNull();
        assertThat(notaFiscalSalva.getId()).isNotNull();
        assertThat(notaFiscalSalva.getNomeArquivo()).isEqualTo("fornecedor-123.pdf");
        assertThat(notaFiscalSalva.getFornecedor().getId()).isEqualTo(fornecedorSalvo.getId());
    }
}
