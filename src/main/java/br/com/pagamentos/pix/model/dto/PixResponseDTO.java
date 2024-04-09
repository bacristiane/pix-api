package br.com.pagamentos.pix.model.dto;

import java.time.LocalDate;
import br.com.pagamentos.pix.model.constant.FrequenciaRecorrencia;
import br.com.pagamentos.pix.model.constant.Status;
import lombok.Data;

@Data
public class PixResponseDTO {

    private Status status;
    private LocalDate dataInclusao;
    private LocalDate dataPagamento;
    private double valor;
    private String descricao;
    private LocalDate dataFinal;
    private FrequenciaRecorrencia frequencia;
    private String chavePix;


   

  

}
