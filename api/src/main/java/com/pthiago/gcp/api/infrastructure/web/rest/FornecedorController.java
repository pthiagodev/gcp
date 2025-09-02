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

@RestController
@RequestMapping("/api/fornecedores")
public class FornecedorController {

    private final FornecedorUseCase fornecedorUseCase;

    public FornecedorController(FornecedorUseCase fornecedorUseCase) {
        this.fornecedorUseCase = fornecedorUseCase;
    }

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

    @GetMapping
    public ResponseEntity<List<FornecedorResponseDTO>> buscarTodosFornecedores() {
        return ResponseEntity.ok(fornecedorUseCase.buscarTodosFornecedores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FornecedorResponseDTO> buscarFornecedorPorId(@PathVariable Long id) {
        return ResponseEntity.ok(fornecedorUseCase.buscarFornecedorPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FornecedorResponseDTO> atualizarFornecedor(@PathVariable Long id, @RequestBody @Valid FornecedorRequestDTO requestDTO) {
        return ResponseEntity.ok(fornecedorUseCase.atualizarFornecedor(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarFornecedor(@PathVariable Long id) {
        fornecedorUseCase.deletarFornecedor(id);
        return ResponseEntity.noContent().build();
    }
}
