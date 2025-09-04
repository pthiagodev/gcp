package com.pthiago.gcp.api.infrastructure.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pthiago.gcp.api.AbstractIntegrationTest;
import com.pthiago.gcp.api.domain.dto.FornecedorRequestDTO;
import com.pthiago.gcp.api.domain.model.Fornecedor;
import com.pthiago.gcp.api.factory.FornecedorFactory;
import com.pthiago.gcp.api.infrastructure.persistence.adapter.FornecedorRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@DisplayName("🧪 Testes de Integração para o FornecedorController")
class FornecedorControllerITest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FornecedorRepositoryAdapter fornecedorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${TEST_CNPJ}")
    private String cnpjValido;

    @Nested
    @DisplayName("Quando criar um novo fornecedor (POST /api/fornecedores)")
    class CriarFornecedor {

        @Test
        @DisplayName("✅ Deve criar um fornecedor com sucesso com dados válidos")
        void deveCriarFornecedorComSucesso() throws Exception {
            FornecedorRequestDTO requestDTO = FornecedorFactory.criarFornecedorRequestDTO(cnpjValido);

            mockMvc.perform(post("/api/fornecedores")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.nome", is(requestDTO.nome())))
                    .andExpect(jsonPath("$.email", is(requestDTO.email())));
        }

        @Test
        @DisplayName("❌ Deve retornar 400 Bad Request com dados inválidos")
        void deveRetornarBadRequestComDadosInvalidos() throws Exception {
            FornecedorRequestDTO requestDTO = new FornecedorRequestDTO(
                    "",
                    "99.999.999/0001-99",
                    "telefone-invalido",
                    "email-invalido"
            );

            mockMvc.perform(post("/api/fornecedores")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDTO)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.validationErrors.nome").exists())
                    .andExpect(jsonPath("$.validationErrors.telefone").exists())
                    .andExpect(jsonPath("$.validationErrors.email").exists());
        }
    }

    @Nested
    @DisplayName("Quando buscar todos os fornecedores (GET /api/fornecedores)")
    class BuscarTodos {

        @Test
        @DisplayName("✅ Deve retornar uma lista de fornecedores")
        void deveRetornarListaDeFornecedores() throws Exception {
            Fornecedor f1 = FornecedorFactory.criarFornecedor(cnpjValido);

            Fornecedor f2 = new Fornecedor();
            f2.setNome("Outro Fornecedor");
            f2.setCnpj("11.222.333/0001-44");
            f2.setTelefone("(11) 98877-6655");
            f2.setEmail("outro@teste.com");

            fornecedorRepository.salvar(f1);
            fornecedorRepository.salvar(f2);

            mockMvc.perform(get("/api/fornecedores"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].nome", is(f1.getNome())));
        }
    }

    @Nested
    @DisplayName("Quando deletar um fornecedor (DELETE /api/fornecedores/{id})")
    class DeletarFornecedor {

        @Test
        @DisplayName("✅ Deve deletar um fornecedor existente com sucesso")
        void deveDeletarFornecedorComSucesso() throws Exception {
            Fornecedor fornecedor = fornecedorRepository.salvar(FornecedorFactory.criarFornecedor(cnpjValido));

            mockMvc.perform(delete("/api/fornecedores/" + fornecedor.getId()))
                    .andExpect(status().isNoContent());

            assertThat(fornecedorRepository.buscarPeloId(fornecedor.getId())).isEmpty();
        }

        @Test
        @DisplayName("❌ Deve retornar 404 Not Found ao tentar deletar um fornecedor inexistente")
        void deveRetornarNotFoundParaFornecedorInexistente() throws Exception {
            mockMvc.perform(delete("/api/fornecedores/999"))
                    .andExpect(status().isNotFound());
        }
    }
}