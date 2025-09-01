package com.pthiago.gcp.api.infrastructure.web.rest;

import com.pthiago.gcp.api.application.port.in.FornecedorUseCase;
import com.pthiago.gcp.api.domain.dto.FornecedorRequestDTO;
import com.pthiago.gcp.api.domain.dto.FornecedorResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/fornecedores")
public class FornecedorController {

    private final FornecedorUseCase fornecedorUseCase;

    public FornecedorController(FornecedorUseCase fornecedorUseCase) {
        this.fornecedorUseCase = fornecedorUseCase;
    }

    @PostMapping
    public ResponseEntity<FornecedorResponseDTO> criarFornecedor(@RequestBody @Valid FornecedorRequestDTO requestDTO) {
        var fornecedor = fornecedorUseCase.criarFornecedor(requestDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(fornecedor.getId())
                .toUri();

        return ResponseEntity.created(location).body(FornecedorResponseDTO.fromEntity(fornecedor));
    }

    @GetMapping
    public ResponseEntity<List<FornecedorResponseDTO>> buscarTodosFornecedores() {
        List<FornecedorResponseDTO> fornecedores = fornecedorUseCase.buscarTodosFornecedores().stream()
                .map(FornecedorResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(fornecedores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FornecedorResponseDTO> buscarFornecedorPorId(@PathVariable Long id) {
        return fornecedorUseCase.buscarFornecedorPorId(id)
                .map(fornecedor -> ResponseEntity.ok(FornecedorResponseDTO.fromEntity(fornecedor)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<FornecedorResponseDTO> atualizarFornecedor(@PathVariable Long id, @RequestBody @Valid FornecedorRequestDTO requestDTO) {
        return fornecedorUseCase.atualizarFornecedor(id, requestDTO)
                .map(fornecedor -> ResponseEntity.ok(FornecedorResponseDTO.fromEntity(fornecedor)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarFornecedor(@PathVariable Long id) {
        if (fornecedorUseCase.deletarFornecedor(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
