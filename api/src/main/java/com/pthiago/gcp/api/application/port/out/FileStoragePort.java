package com.pthiago.gcp.api.application.port.out;

import com.pthiago.gcp.api.domain.dto.ArquivoInfoDTO;

import java.nio.file.Path;

public interface FileStoragePort {
    Path salvar(String nomeFinal, ArquivoInfoDTO arquivoInfo);
}
