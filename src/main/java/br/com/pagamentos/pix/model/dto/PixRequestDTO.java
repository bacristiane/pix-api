package br.com.pagamentos.pix.model.dto;

import java.time.LocalDate;



import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.pagamentos.pix.model.constant.FrequenciaRecorrencia;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class PixRequestDTO {

  
    @NotNull(message = "A data de pagamento é obrigatória")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @FutureOrPresent(message = "A data de pagamento deve ser no presente ou futuro")
    private LocalDate dataPagamento;


    @NotNull(message = "O valor é obrigatório")
    @Positive(message = "O valor do pagamento deve ser positivo")
    private double valor;
    
    private String descricao;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dataFinal;

    private FrequenciaRecorrencia frequencia;


    @NotBlank(message = "A chave pix é obrigatória")
    @Pattern(regexp = "^(?:\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\(\\d{2}\\)\\s\\d{4,5}-\\d{4}|[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}|[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12})$",message = "A chave pix deve ser um CPF, e-mail, telefone ou UUID")
    private String chavePix;

  

}
