package com.pthiago.gcp.api.application.service;

import com.pthiago.gcp.api.application.port.out.FornecedorRepositoryPort;
import com.pthiago.gcp.api.domain.dto.FornecedorRequestDTO;
import com.pthiago.gcp.api.domain.dto.FornecedorResponseDTO;
import com.pthiago.gcp.api.domain.exception.ResourceNotFoundException;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("🧪 Testes do Serviço de Fornecedores")
class FornecedorServiceImplTest {

    @Mock
    private FornecedorRepositoryPort fornecedorRepositoryPort;

    @InjectMocks
    private FornecedorServiceImpl fornecedorService;

    @Nested
    @DisplayName("Quando criar um novo fornecedor")
    class CriarFornecedor {
        @Test
        @DisplayName("✅ Deve criar e retornar o DTO com sucesso")
        void deveCriarFornecedorComSucesso() {
            FornecedorRequestDTO requestDTO = new FornecedorRequestDTO("Novo Fornecedor", "11.222.333/0001-44", "(85) 99999-1111", "novo@fornecedor.com");
            Fornecedor fornecedorSalvo = new Fornecedor();
            fornecedorSalvo.setId(1L);
            fornecedorSalvo.setNome(requestDTO.nome());

            when(fornecedorRepositoryPort.salvar(any(Fornecedor.class))).thenReturn(fornecedorSalvo);

            FornecedorResponseDTO responseDTO = fornecedorService.criarFornecedor(requestDTO);

            assertThat(responseDTO).isNotNull();
            assertThat(responseDTO.id()).isEqualTo(1L);
            verify(fornecedorRepositoryPort, times(1)).salvar(any(Fornecedor.class));
        }
    }

    @Nested
    @DisplayName("Quando buscar um fornecedor por ID")
    class BuscarPorId {
        @Test
        @DisplayName("✅ Deve retornar o fornecedor quando o ID existe")
        void deveRetornarFornecedorQuandoIdExiste() {
            Fornecedor fornecedorExistente = new Fornecedor();
            fornecedorExistente.setId(1L);
            when(fornecedorRepositoryPort.buscarPeloId(1L)).thenReturn(Optional.of(fornecedorExistente));

            FornecedorResponseDTO responseDTO = fornecedorService.buscarFornecedorPorId(1L);

            assertThat(responseDTO).isNotNull();
            assertThat(responseDTO.id()).isEqualTo(1L);
        }

        @Test
        @DisplayName("❌ Deve lançar ResourceNotFoundException quando o ID não existe")
        void deveLancarExcecaoQuandoIdNaoExiste() {
            when(fornecedorRepositoryPort.buscarPeloId(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> fornecedorService.buscarFornecedorPorId(99L))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Quando listar todos os fornecedores")
    class ListarTodos {
        @Test
        @DisplayName("✅ Deve retornar uma lista de fornecedores")
        void deveRetornarListaDeFornecedores() {
            when(fornecedorRepositoryPort.listar()).thenReturn(List.of(new Fornecedor(), new Fornecedor()));
            List<FornecedorResponseDTO> response = fornecedorService.buscarTodosFornecedores();
            assertThat(response).hasSize(2);
        }

        @Test
        @DisplayName("✅ Deve retornar uma lista vazia se não houver fornecedores")
        void deveRetornarListaVazia() {
            when(fornecedorRepositoryPort.listar()).thenReturn(Collections.emptyList());
            List<FornecedorResponseDTO> response = fornecedorService.buscarTodosFornecedores();
            assertThat(response).isEmpty();
        }
    }

    @Nested
    @DisplayName("Quando atualizar um fornecedor existente")
    class AtualizarFornecedor {
        @Test
        @DisplayName("✅ Deve atualizar o fornecedor com sucesso")
        void deveAtualizarFornecedorComSucesso() {
            FornecedorRequestDTO requestDTO = new FornecedorRequestDTO("Fornecedor Atualizado", "55.666.777/0001-88", "(85) 99999-2222", "atualizado@fornecedor.com");
            Fornecedor fornecedorExistente = new Fornecedor();
            fornecedorExistente.setId(1L);

            when(fornecedorRepositoryPort.buscarPeloId(1L)).thenReturn(Optional.of(fornecedorExistente));
            when(fornecedorRepositoryPort.salvar(any(Fornecedor.class))).thenAnswer(invocation -> invocation.getArgument(0));

            FornecedorResponseDTO responseDTO = fornecedorService.atualizarFornecedor(1L, requestDTO);

            assertThat(responseDTO.nome()).isEqualTo("Fornecedor Atualizado");
            verify(fornecedorRepositoryPort, times(1)).salvar(any(Fornecedor.class));
        }

        @Test
        @DisplayName("❌ Deve lançar ResourceNotFoundException ao tentar atualizar um ID que não existe")
        void deveLancarExcecaoAoAtualizarIdNaoExistente() {
            FornecedorRequestDTO requestDTO = new FornecedorRequestDTO("Nome", "11.222.333/0001-44", "(85) 99999-1111", "email@test.com");
            when(fornecedorRepositoryPort.buscarPeloId(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> fornecedorService.atualizarFornecedor(99L, requestDTO))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Quando deletar um fornecedor")
    class DeletarFornecedor {
        @Test
        @DisplayName("✅ Deve deletar o fornecedor com sucesso")
        void deveDeletarFornecedorComSucesso() {
            when(fornecedorRepositoryPort.existePorId(1L)).thenReturn(true);
            doNothing().when(fornecedorRepositoryPort).deletar(1L);

            fornecedorService.deletarFornecedor(1L);

            verify(fornecedorRepositoryPort, times(1)).deletar(1L);
        }

        @Test
        @DisplayName("❌ Deve lançar ResourceNotFoundException ao tentar deletar um ID que não existe")
        void deveLancarExcecaoAoDeletarIdNaoExistente() {
            when(fornecedorRepositoryPort.existePorId(99L)).thenReturn(false);

            assertThatThrownBy(() -> fornecedorService.deletarFornecedor(99L))
                    .isInstanceOf(ResourceNotFoundException.class);
            verify(fornecedorRepositoryPort, never()).deletar(anyLong());
        }
    }
}

