package com.pthiago.gcp.api.infrastructure.web.rest;

import com.pthiago.gcp.api.application.port.in.FornecedorUseCase;
import com.pthiago.gcp.api.domain.dto.FornecedorRequestDTO;
import com.pthiago.gcp.api.domain.dto.FornecedorResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/fornecedores")
@Tag(name = "Fornecedores", description = "Endpoints para o gerenciamento de fornecedores")
public class FornecedorController {

    private final FornecedorUseCase fornecedorUseCase;

    public FornecedorController(FornecedorUseCase fornecedorUseCase) {
        this.fornecedorUseCase = fornecedorUseCase;
    }

    @Operation(summary = "Criar um novo fornecedor", description = "Registra um novo fornecedor no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Fornecedor criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<FornecedorResponseDTO> criarFornecedor(@RequestBody @Valid FornecedorRequestDTO requestDTO) {
        var responseDTO = fornecedorUseCase.criarFornecedor(requestDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDTO.id())
                .toUri();

        return ResponseEntity.created(location).body(responseDTO);
    }

    @Operation(summary = "Listar todos os fornecedores", description = "Retorna uma lista com todos os fornecedores cadastrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de fornecedores retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<FornecedorResponseDTO>> buscarTodosFornecedores() {
        return ResponseEntity.ok(fornecedorUseCase.buscarTodosFornecedores());
    }

    @Operation(summary = "Buscar um fornecedor por ID", description = "Retorna os detalhes de um fornecedor específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fornecedor encontrado"),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FornecedorResponseDTO> buscarFornecedorPorId(@PathVariable Long id) {
        return ResponseEntity.ok(fornecedorUseCase.buscarFornecedorPorId(id));
    }

    @Operation(summary = "Atualizar um fornecedor existente", description = "Atualiza os dados de um fornecedor a partir do seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fornecedor atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<FornecedorResponseDTO> atualizarFornecedor(@PathVariable Long id, @RequestBody @Valid FornecedorRequestDTO requestDTO) {
        return ResponseEntity.ok(fornecedorUseCase.atualizarFornecedor(id, requestDTO));
    }

    @Operation(summary = "Deletar um fornecedor", description = "Remove um fornecedor do sistema a partir do seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Fornecedor deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarFornecedor(@PathVariable Long id) {
        fornecedorUseCase.deletarFornecedor(id);
        return ResponseEntity.noContent().build();
    }
}
