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
        Pix pix = new Pix();
        pix.setDataPagamento(pixDTO.getDataPagamento());
        pix.setValor(pixDTO.getValor());
        pix.setDescricao(pixDTO.getDescricao());
        pix.setRecorrencia(mapToRecorrencia(pixDTO));
        pix.setDestinoPix(mapToDestino(pixDTO));
        return pix;
    }

    public Pix mapToUpdateEntity(PixRequestDTO pixDTO, Pix pix) {
        
        pix.setDataPagamento(pixDTO.getDataPagamento());
        pix.setValor(pixDTO.getValor());
        pix.setDescricao(pixDTO.getDescricao());
        pix.setRecorrencia(mapToRecorrencia(pixDTO));
        pix.setDestinoPix(mapToDestino(pixDTO));
        return pix;
    }


    public RecorrenciaPix mapToRecorrencia(PixRequestDTO pixDTO) {
        RecorrenciaPix recorrencia = new RecorrenciaPix();
        recorrencia.setDataFinal(pixDTO.getDataFinal());
        recorrencia.setFrequencia(pixDTO.getFrequencia());
        return recorrencia;
    }

    public Destino mapToDestino(PixRequestDTO pixDTO) {
        Destino destino = new Destino();
        destino.setChavePix(pixDTO.getChavePix());
        return destino;
        
    }



    public PixResponseDTO mapToDTO(Pix pix) {
        PixResponseDTO pixDTO = new PixResponseDTO();
        pixDTO.setStatus(pix.getStatus());
        pixDTO.setDataInclusao(pix.getDataInclusao());
        pixDTO.setDataPagamento(pix.getDataPagamento());
        pixDTO.setValor(pix.getValor());
        pixDTO.setDescricao(pix.getDescricao());
        pixDTO.setDataFinal(Optional.ofNullable(pix.getRecorrencia()).map(RecorrenciaPix::getDataFinal).orElse(null));
        pixDTO.setFrequencia(Optional.ofNullable(pix.getRecorrencia()).map(RecorrenciaPix::getFrequencia).orElse(null));
        pixDTO.setChavePix(pix.getDestinoPix().getChavePix());
        return pixDTO;
    }

    public List<PixResponseDTO> mapPixListToDTOs(List<Pix> pixList) {
        return pixList.stream().map(this::mapToDTO).collect(Collectors.toList());
    }
    
}
