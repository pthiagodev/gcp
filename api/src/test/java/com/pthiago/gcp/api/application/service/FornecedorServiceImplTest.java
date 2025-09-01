package com.pthiago.gcp.api.application.service;

import com.pthiago.gcp.api.application.port.out.FornecedorRepositoryPort;
import com.pthiago.gcp.api.domain.dto.FornecedorRequestDTO;
import com.pthiago.gcp.api.domain.model.Fornecedor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("🧪 Testes do Serviço de Gerenciamento de Fornecedores")
class FornecedorServiceImplTest {

    @Mock
    private FornecedorRepositoryPort fornecedorRepositoryPort;

    @InjectMocks
    private FornecedorServiceImpl fornecedorService;

    @Nested
    @DisplayName("Criação de Fornecedor")
    class CriacaoFornecedor {
        @Test
        @DisplayName("✅ Deve criar um novo fornecedor com sucesso")
        void deveCriarFornecedorComSucesso() {
            var requestDTO = new FornecedorRequestDTO("Fornecedor Teste", "12.345.678/0001-99", "(85) 99999-8888", "teste@teste.com");
            var fornecedorSalvo = new Fornecedor();
            fornecedorSalvo.setId(1L);
            fornecedorSalvo.setNome(requestDTO.nome());
            fornecedorSalvo.setCnpj(requestDTO.cnpj());
            fornecedorSalvo.setEmail(requestDTO.email());
            fornecedorSalvo.setTelefone(requestDTO.telefone());

            when(fornecedorRepositoryPort.salvar(any(Fornecedor.class))).thenReturn(fornecedorSalvo);

            Fornecedor resultado = fornecedorService.criarFornecedor(requestDTO);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(1L);
            assertThat(resultado.getNome()).isEqualTo("Fornecedor Teste");
            assertThat(resultado.getTelefone()).isEqualTo("(85) 99999-8888");
            verify(fornecedorRepositoryPort, times(1)).salvar(any(Fornecedor.class));
        }
    }

    @Nested
    @DisplayName("Busca de Fornecedores")
    class BuscaFornecedores {
        @Test
        @DisplayName("✅ Deve encontrar um fornecedor pelo ID quando ele existe")
        void deveEncontrarFornecedorPorId() {
            var fornecedorExistente = new Fornecedor();
            fornecedorExistente.setId(1L);
            when(fornecedorRepositoryPort.buscarPeloId(1L)).thenReturn(Optional.of(fornecedorExistente));

            Optional<Fornecedor> resultado = fornecedorService.buscarFornecedorPorId(1L);

            assertThat(resultado).isPresent();
            assertThat(resultado.get().getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("🟡 Deve retornar vazio ao buscar um fornecedor por ID que não existe")
        void deveRetornarVazioSeFornecedorNaoExiste() {
            when(fornecedorRepositoryPort.buscarPeloId(99L)).thenReturn(Optional.empty());

            Optional<Fornecedor> resultado = fornecedorService.buscarFornecedorPorId(99L);

            assertThat(resultado).isNotPresent();
        }

        @Test
        @DisplayName("✅ Deve retornar uma lista de todos os fornecedores")
        void deveRetornarTodosFornecedores() {
            var fornecedor1 = new Fornecedor();
            var fornecedor2 = new Fornecedor();
            when(fornecedorRepositoryPort.listar()).thenReturn(List.of(fornecedor1, fornecedor2));

            List<Fornecedor> resultado = fornecedorService.buscarTodosFornecedores();

            assertThat(resultado).hasSize(2);
        }

        @Test
        @DisplayName("🟡 Deve retornar uma lista vazia se não houver fornecedores")
        void deveRetornarListaVazia() {
            when(fornecedorRepositoryPort.listar()).thenReturn(Collections.emptyList());

            List<Fornecedor> resultado = fornecedorService.buscarTodosFornecedores();

            assertThat(resultado).isEmpty();
        }
    }

    @Nested
    @DisplayName("Atualização de Fornecedor")
    class AtualizacaoFornecedor {
        @Test
        @DisplayName("✅ Deve atualizar um fornecedor existente com sucesso")
        void deveAtualizarFornecedorComSucesso() {
            var requestDTO = new FornecedorRequestDTO("Nome Atualizado", "98.765.432/0001-11", "(85) 91111-2222", "email@atualizado.com");
            var fornecedorExistente = new Fornecedor();
            fornecedorExistente.setId(1L);

            when(fornecedorRepositoryPort.buscarPeloId(1L)).thenReturn(Optional.of(fornecedorExistente));
            when(fornecedorRepositoryPort.salvar(any(Fornecedor.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Optional<Fornecedor> resultado = fornecedorService.atualizarFornecedor(1L, requestDTO);

            assertThat(resultado).isPresent();
            assertThat(resultado.get().getNome()).isEqualTo("Nome Atualizado");
            assertThat(resultado.get().getEmail()).isEqualTo("email@atualizado.com");
            assertThat(resultado.get().getTelefone()).isEqualTo("(85) 91111-2222");
            verify(fornecedorRepositoryPort, times(1)).salvar(fornecedorExistente);
        }

        @Test
        @DisplayName("🟡 Deve retornar vazio ao tentar atualizar um fornecedor que não existe")
        void deveRetornarVazioAoAtualizarFornecedorInexistente() {
            var requestDTO = new FornecedorRequestDTO("Nome Valido", "11.111.111/1111-11", "(11) 11111-1111", "email@valido.com");
            when(fornecedorRepositoryPort.buscarPeloId(99L)).thenReturn(Optional.empty());

            Optional<Fornecedor> resultado = fornecedorService.atualizarFornecedor(99L, requestDTO);

            assertThat(resultado).isNotPresent();
            verify(fornecedorRepositoryPort, never()).salvar(any());
        }
    }

    @Nested
    @DisplayName("Exclusão de Fornecedor")
    class ExclusaoFornecedor {
        @Test
        @DisplayName("✅ Deve retornar true ao deletar um fornecedor que existe")
        void deveDeletarFornecedorExistente() {
            when(fornecedorRepositoryPort.existePorId(1L)).thenReturn(true);
            doNothing().when(fornecedorRepositoryPort).deletar(1L);

            boolean resultado = fornecedorService.deletarFornecedor(1L);

            assertThat(resultado).isTrue();
            verify(fornecedorRepositoryPort, times(1)).deletar(1L);
        }

        @Test
        @DisplayName("🟡 Deve retornar false ao tentar deletar um fornecedor que não existe")
        void deveRetornarFalseAoDeletarFornecedorInexistente() {
            when(fornecedorRepositoryPort.existePorId(99L)).thenReturn(false);

            boolean resultado = fornecedorService.deletarFornecedor(99L);

            assertThat(resultado).isFalse();
            verify(fornecedorRepositoryPort, never()).deletar(anyLong());
        }
    }
}

