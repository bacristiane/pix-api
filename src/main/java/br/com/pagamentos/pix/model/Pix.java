package br.com.pagamentos.pix.model;


import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

import br.com.pagamentos.pix.model.constant.Status;

@Data
@Entity
public class Pix {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Status status;

    
    private LocalDate dataInclusao;

    private LocalDate dataPagamento;

    private double valor;

    private String descricao;

    @Embedded
    private RecorrenciaPix recorrencia;

    @Embedded
    private Destino destinoPix;

}
