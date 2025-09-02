package com.pthiago.gcp.api.infrastructure.web.rest;


import com.pthiago.gcp.api.application.port.in.NotaFiscalUseCase;
import com.pthiago.gcp.api.domain.dto.ArquivoInfoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api/notasfiscais")
@Tag(name = "Notas Fiscais", description = "Endpoints para o envio e gerenciamento de notas fiscais")
public class NotaFiscalController {

    private final NotaFiscalUseCase notaFiscalUseCase;

    public NotaFiscalController(NotaFiscalUseCase notaFiscalUseCase) {
        this.notaFiscalUseCase = notaFiscalUseCase;
    }

    @Operation(
            summary = "Upload e envio de uma nota fiscal",
            description = "Recebe um arquivo PDF, associa a um fornecedor e número de NF, salva o arquivo e envia por e-mail."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nota fiscal enviada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou erro ao ler o arquivo", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/upload")
    public ResponseEntity<String> uploadNotaFiscal(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fornecedorId") Long fornecedorId,
            @RequestParam("numeroNF") String numeroNF) {
        try {
            ArquivoInfoDTO arquivoInfo = new ArquivoInfoDTO(file.getOriginalFilename(), file.getInputStream());
            notaFiscalUseCase.salvarEEnviarNotaFiscal(fornecedorId, numeroNF, arquivoInfo);
            return ResponseEntity.ok("Nota fiscal enviada com sucesso!");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Erro ao ler o arquivo.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Falha ao enviar nota fiscal: " + e.getMessage());
        }
    }
}
