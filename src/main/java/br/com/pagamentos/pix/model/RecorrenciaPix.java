package br.com.pagamentos.pix.model;

import java.time.LocalDate;

import br.com.pagamentos.pix.model.constant.FrequenciaRecorrencia;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Embeddable
public class RecorrenciaPix {

    private LocalDate dataFinal;

    @Enumerated(EnumType.STRING)
    private FrequenciaRecorrencia frequencia;
    
}
