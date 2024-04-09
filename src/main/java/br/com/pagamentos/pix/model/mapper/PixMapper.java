package br.com.pagamentos.pix.model.mapper;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;



import br.com.pagamentos.pix.model.Destino;
import br.com.pagamentos.pix.model.Pix;
import br.com.pagamentos.pix.model.RecorrenciaPix;
import br.com.pagamentos.pix.model.dto.PixRequestDTO;
import br.com.pagamentos.pix.model.dto.PixResponseDTO;

import org.springframework.stereotype.Component;


@Component
public class PixMapper {

    public Pix mapToCreateEntity(PixRequestDTO pixDTO) {
       return Pix.builder().dataPagamento(pixDTO.getDataPagamento()).valor(pixDTO.getValor()).descricao(pixDTO.getDescricao()).recorrencia(mapToRecorrencia(pixDTO)).destinoPix(mapToDestino(pixDTO)).build();
       
    }

    public Pix mapToUpdateEntity(PixRequestDTO pixDTO, Pix pix) {
        
        return Pix.builder().dataPagamento(pixDTO.getDataPagamento()).valor(pixDTO.getValor()).descricao(pixDTO.getDescricao()).recorrencia(mapToRecorrencia(pixDTO)).destinoPix(mapToDestino(pixDTO)).build();

    }


    public RecorrenciaPix mapToRecorrencia(PixRequestDTO pixDTO) {
       return RecorrenciaPix.builder().dataFinal(pixDTO.getDataFinal()).frequencia(pixDTO.getFrequencia()).build();}
        

    public Destino mapToDestino(PixRequestDTO pixDTO) {

        return Destino.builder().chavePix(pixDTO.getChavePix()).build(); 
        
    }



    public PixResponseDTO mapToDTO(Pix pix) {

        
        return PixResponseDTO.builder().status(pix.getStatus()).dataInclusao(pix.getDataInclusao()).dataPagamento(pix.getDataPagamento()).valor(pix.getValor()).descricao(pix.getDescricao()).dataFinal(Optional.ofNullable(pix.getRecorrencia()).map(RecorrenciaPix::getDataFinal).orElse(null)).frequencia(Optional.ofNullable(pix.getRecorrencia()).map(RecorrenciaPix::getFrequencia).orElse(null)).chavePix(pix.getDestinoPix().getChavePix()).build();
       
    }

    public List<PixResponseDTO> mapPixListToDTOs(List<Pix> pixList) {
        return pixList.stream().map(this::mapToDTO).collect(Collectors.toList());
    }
    
}
