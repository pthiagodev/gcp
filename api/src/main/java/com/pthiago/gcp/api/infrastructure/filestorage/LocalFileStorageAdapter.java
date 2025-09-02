package com.pthiago.gcp.api.infrastructure.filestorage;

import com.pthiago.gcp.api.application.port.out.FileStoragePort;
import com.pthiago.gcp.api.domain.dto.ArquivoInfoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

@Component
public class LocalFileStorageAdapter implements FileStoragePort {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public Path salvar(String nomeFinal, ArquivoInfoDTO arquivoInfo) {
        try {
            Path diretorioFinal = criarEstruturaDeDiretorios();
            Path caminhoCompleto = diretorioFinal.resolve(nomeFinal);
            Files.copy(arquivoInfo.conteudo(), caminhoCompleto, StandardCopyOption.REPLACE_EXISTING);
            return caminhoCompleto;
        } catch (IOException e) {
            throw new RuntimeException("Falha ao salvar arquivo no servidor.", e);
        }
    }

    private Path criarEstruturaDeDiretorios() throws IOException {
        LocalDate hoje = LocalDate.now();
        String ano = String.valueOf(hoje.getYear());
        String mes = String.format("%02d-%s", hoje.getMonthValue(),
                hoje.getMonth().getDisplayName(TextStyle.FULL, Locale.of("pt", "BR")));
        String dia = String.format("%02d", hoje.getDayOfMonth());

        Path diretorioFinal = Paths.get(uploadDir, ano, mes, dia);
        Files.createDirectories(diretorioFinal);
        return diretorioFinal;
    }
}
