package com.pthiago.gcp.api.application.port.in;

import com.pthiago.gcp.api.domain.dto.ArquivoInfoDTO;

public interface NotaFiscalUseCase {
    void salvarEEnviarNotaFiscal(Long fornecedorId, String numeroNF,ArquivoInfoDTO arquivoInfoDTO);
}
