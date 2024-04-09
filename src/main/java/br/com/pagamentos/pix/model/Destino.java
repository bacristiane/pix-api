package br.com.pagamentos.pix.model;

import br.com.pagamentos.pix.model.constant.TipoChave;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
@Embeddable
public class Destino {


    private String chavePix;
    
    @Enumerated(EnumType.STRING)
    private TipoChave tipoChavePix;

}
