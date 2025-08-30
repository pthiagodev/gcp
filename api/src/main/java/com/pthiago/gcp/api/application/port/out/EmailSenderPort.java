package com.pthiago.gcp.api.application.port.out;

import java.io.File;

public interface EmailSenderPort {
    void enviarEmailComAnexo(String para, String assunto, String corpo, File anexo);
}
