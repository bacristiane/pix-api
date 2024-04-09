package br.com.pagamentos.pix.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PixWrapperDTO<T> {

    private T data;
    private String message;



    public PixWrapperDTO(T data) {
        this.data = data;
    }

}

