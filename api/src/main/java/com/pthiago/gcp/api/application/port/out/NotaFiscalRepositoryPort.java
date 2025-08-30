package com.pthiago.gcp.api.application.port.out;

import com.pthiago.gcp.api.domain.model.NotaFiscal;

public interface NotaFiscalRepositoryPort {
    NotaFiscal salvar(NotaFiscal notaFiscal);
}
