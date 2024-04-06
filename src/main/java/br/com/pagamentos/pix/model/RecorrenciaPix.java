package br.com.pagamentos.pix.model;

import java.time.LocalDate;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class RecorrenciaPix {

    private LocalDate dataFinal;
    private FrequenciaRecorrencia frequencia;
    
}
