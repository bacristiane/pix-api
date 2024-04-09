package br.com.pagamentos.pix.utils;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import br.com.pagamentos.pix.exceptions.ChavePixInvalidException;
import br.com.pagamentos.pix.model.constant.Status;
import br.com.pagamentos.pix.model.constant.TipoChave;

@Component
public class PixUtils {

    

    public  TipoChave verificarTipoChavePix(String chave) {
        if (chave.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            return TipoChave.EMAIL;
        }
        if (chave.matches("^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$")) {
            return TipoChave.CPF;
        }
        if (chave.matches("^\\(\\d{2}\\)\\s\\d{4,5}-\\d{4}$")) {
            return TipoChave.TELEFONE;
        }
        if (chave.matches("^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$")) {
            return TipoChave.ALEATORIA;
        }
        throw new ChavePixInvalidException("Chave Pix inválida: " + chave);
    }



    public  Status definirStatus(LocalDate dataPagamento) {
        if (dataPagamento.isEqual(LocalDate.now())) {
            return Status.EFETUADO;
        } else {
            return Status.AGENDADO;
        }
    }
}
