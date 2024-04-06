package br.com.pagamentos.pix.model;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class PixDTO {

    private Long id;

    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataInclusao;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataPagamento;

    private double valor;
    
    private String descricao;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataFinal;

    private FrequenciaRecorrencia frequencia;

    private String chavePix;

  

}
