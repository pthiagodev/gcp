package com.pthiago.gcp.api.application.port.in;

import com.pthiago.gcp.api.domain.dto.ArquivoInfoDTO;
import com.pthiago.gcp.api.domain.exception.ResourceNotFoundException;

/**
 * Contrato (Porta de Entrada) para os casos de uso relacionados ao processamento de Notas Fiscais.
 * Define as operações de negócio que a aplicação expõe para o upload e envio de notas.
 */
public interface NotaFiscalUseCase {

    /**
     * Orquestra o processo completo de recebimento de uma nota fiscal, salvando o arquivo,
     * registrando a operação no banco de dados e enviando uma notificação por e-mail para o fornecedor.
     *
     * @param fornecedorId O ID do fornecedor ao qual a nota fiscal pertence.
     * @param numeroNF O número da nota fiscal informado pelo usuário.
     * @param arquivoInfo DTO contendo o nome original e o conteúdo (InputStream) do arquivo PDF.
     * @throws ResourceNotFoundException se nenhum fornecedor for encontrado com o ID fornecido.
     */
    void salvarEEnviarNotaFiscal(Long fornecedorId, String numeroNF, ArquivoInfoDTO arquivoInfo);
}
