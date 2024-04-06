package br.com.pagamentos.pix.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class DestinoPix {

    private String chavePix;

    private String tipoChavePix;

}
