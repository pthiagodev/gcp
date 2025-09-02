package com.pthiago.gcp.api.infrastructure.web.rest;


import com.pthiago.gcp.api.application.port.in.NotaFiscalUseCase;
import com.pthiago.gcp.api.domain.dto.ArquivoInfoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api/notasfiscais")
public class NotaFiscalController {

    private final NotaFiscalUseCase notaFiscalUseCase;

    public NotaFiscalController(NotaFiscalUseCase notaFiscalUseCase) {
        this.notaFiscalUseCase = notaFiscalUseCase;
    }

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
