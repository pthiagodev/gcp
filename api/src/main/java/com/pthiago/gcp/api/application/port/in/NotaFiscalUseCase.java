package com.pthiago.gcp.api.application.port.in;

import com.pthiago.gcp.api.domain.dto.ArquivoInfoDTO;

public interface NotaFiscalUseCase {

    /**
     * Fluxo principal para salvar e enviar uma nota fiscal.
     *
     * @param fornecedorId ID do fornecedor
     * @param numeroNF Número da nota fiscal
     * @param arquivoInfoDTO Informações do arquivo da nota fiscal
     */
    void salvarEEnviarNotaFiscal(Long fornecedorId, String numeroNF,ArquivoInfoDTO arquivoInfoDTO);
}
