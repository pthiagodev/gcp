package com.pthiago.gcp.api.infrastructure.persistence.adapter;

import com.pthiago.gcp.api.AbstractIntegrationTest;
import com.pthiago.gcp.api.domain.model.Fornecedor;
import com.pthiago.gcp.api.factory.FornecedorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@DisplayName("🧪 Testes de Integração para o FornecedorRepositoryAdapter")
class FornecedorRepositoryAdapterITest extends AbstractIntegrationTest {

    @Autowired
    private FornecedorRepositoryAdapter fornecedorRepositoryAdapter;

    @Value("${TEST_CNPJ}")
    private String cnpjValido;

    @Test
    @DisplayName("✅ Deve salvar um fornecedor com sucesso")
    void deveSalvarFornecedorComSucesso() {
        Fornecedor novoFornecedor = FornecedorFactory.criarFornecedor(cnpjValido);

        Fornecedor fornecedorSalvo = fornecedorRepositoryAdapter.salvar(novoFornecedor);

        assertThat(fornecedorSalvo).isNotNull();
        assertThat(fornecedorSalvo.getId()).isNotNull();
        assertThat(fornecedorSalvo.getNome()).isEqualTo("Fornecedor de Fábrica");
    }

    @Test
    @DisplayName("✅ Deve buscar um fornecedor pelo ID com sucesso")
    void deveBuscarFornecedorPeloIdComSucesso() {
        Fornecedor fornecedorSalvo = fornecedorRepositoryAdapter.salvar(FornecedorFactory.criarFornecedor(cnpjValido));

        Optional<Fornecedor> fornecedorBuscadoOpt = fornecedorRepositoryAdapter.buscarPeloId(fornecedorSalvo.getId());

        assertThat(fornecedorBuscadoOpt).isPresent();
        assertThat(fornecedorBuscadoOpt.get().getId()).isEqualTo(fornecedorSalvo.getId());
    }

    @Test
    @DisplayName("✅ Deve listar todos os fornecedores")
    void deveListarTodosFornecedores() {
        Fornecedor f1 = FornecedorFactory.criarFornecedor(cnpjValido);

        Fornecedor f2 = new Fornecedor();
        f2.setNome("Fornecedor B");
        f2.setCnpj("91.339.022/0001-03");
        f2.setEmail("b@teste.com");
        f2.setTelefone("(11) 92222-2222");

        fornecedorRepositoryAdapter.salvar(f1);
        fornecedorRepositoryAdapter.salvar(f2);

        List<Fornecedor> fornecedores = fornecedorRepositoryAdapter.listar();

        assertThat(fornecedores).hasSize(2);
        assertThat(fornecedores).extracting(Fornecedor::getNome).containsExactlyInAnyOrder("Fornecedor de Fábrica", "Fornecedor B");
    }
}

