package com.pthiago.gcp.api.domain.dto;

import java.io.InputStream;

public record ArquivoInfoDTO(String nomeOriginal, InputStream conteudo) {
}
