package br.com.pagamentos.pix.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDate;

import br.com.pagamentos.pix.exceptions.PixNotFoundException;
import br.com.pagamentos.pix.messaging.QueueSender;
import br.com.pagamentos.pix.model.Pix;
import br.com.pagamentos.pix.model.constant.Status;
import br.com.pagamentos.pix.model.dto.PixRequestDTO;
import br.com.pagamentos.pix.model.dto.PixResponseDTO;
import br.com.pagamentos.pix.model.dto.PixWrapperDTO;
import br.com.pagamentos.pix.model.mapper.PixMapper;
import br.com.pagamentos.pix.repository.PixRepository;
import br.com.pagamentos.pix.utils.PixUtils;

@Service
public class PixService {


    @Autowired
    private PixRepository pixRepository;

    @Autowired
    private PixMapper modelMapper;

    @Autowired
    private PixUtils pixUtils;

    @Autowired
    private QueueSender queueSender;

    

   
    public PixWrapperDTO<PixResponseDTO> criarPix(PixRequestDTO pixDTO) {

     String mensagem = "";

        boolean pixExistente = pixRepository.existsByValorAndDataPagamentoAndDestinoPix_ChavePix(
                pixDTO.getValor(), pixDTO.getDataPagamento(), pixDTO.getChavePix());
                if (pixExistente) {
                    mensagem = "AVISO:Já existe um Pix com o mesmo valor, data e destino.";
                }

        Pix pix = modelMapper.mapToCreateEntity(pixDTO);

        Status status = pixUtils.definirStatus(pixDTO.getDataPagamento());
        pix.setStatus(status);
        pix.setDataInclusao(LocalDate.now());
        pix.getDestinoPix().setTipoChavePix(pixUtils.verificarTipoChavePix(pixDTO.getChavePix()));
       
        pix = pixRepository.save(pix);


        queueSender.send("Pix criado com sucesso.");
        return (mensagem != null && !mensagem.isEmpty()) ? 
        new PixWrapperDTO<PixResponseDTO>(modelMapper.mapToDTO(pix), mensagem) : 
        new PixWrapperDTO<PixResponseDTO>(modelMapper.mapToDTO(pix));
        

    }
    
    public PixWrapperDTO<PixResponseDTO> buscarPix(Long id) {
        Pix pix = pixRepository.findById(id)
                .orElseThrow(() -> new PixNotFoundException("Pix não encontrado"));
                return new PixWrapperDTO<PixResponseDTO>(modelMapper.mapToDTO(pix));
    }

    
    public PixWrapperDTO<List<PixResponseDTO>> buscarPixs(Status status) {
        List<Pix> pixList = status != null ? pixRepository.findByStatus(status) : pixRepository.findAll();

         List<PixResponseDTO> pixResponseList = modelMapper.mapPixListToDTOs(pixList);

    return new PixWrapperDTO<>(pixResponseList);
    }
    

    public PixWrapperDTO<PixResponseDTO> atualizarPix(Long id, PixRequestDTO pixDTO) {
        Pix pix = pixRepository.findById(id).orElseThrow(() -> new PixNotFoundException("Pix não encontrado."));


        if (pix.getStatus() != Status.AGENDADO) {
            throw new PixNotFoundException("Nenhum pix agendado para atualizar.");
        }


        Status status = pixUtils.definirStatus(pixDTO.getDataPagamento());
        

        pix = modelMapper.mapToUpdateEntity(pixDTO, pix);

        pix.setStatus(status);
        pix.getDestinoPix().setTipoChavePix(pixUtils.verificarTipoChavePix(pixDTO.getChavePix()));


        pix = pixRepository.save(pix);
        queueSender.send("Pix atualizado com sucesso.");
        return new PixWrapperDTO<PixResponseDTO>(modelMapper.mapToDTO(pix));

    }

    public PixWrapperDTO<PixResponseDTO> atualizarParcialmentePix(Long id, PixRequestDTO pixDTO) {
        Pix pix = pixRepository.findById(id).orElseThrow(() -> new PixNotFoundException("Pix não encontrado."));


        if (pix.getStatus() != Status.AGENDADO) {
            throw new PixNotFoundException("Nenhum pix agendado para atualizar.");
        }


        Status status = pixUtils.definirStatus(pixDTO.getDataPagamento());
        

        pix = modelMapper.mapToUpdateEntity(pixDTO, pix);

        pix.setStatus(status);
        pix.getDestinoPix().setTipoChavePix(pixUtils.verificarTipoChavePix(pixDTO.getChavePix()));


        pix = pixRepository.save(pix);
        queueSender.send("Pix atualizado com sucesso.");
        return new PixWrapperDTO<PixResponseDTO>(modelMapper.mapToDTO(pix));

    }

    public PixWrapperDTO<PixResponseDTO> deletarPix(Long id) {
        Pix pix = pixRepository.findById(id).orElseThrow(() -> new PixNotFoundException("Pix não encontrado."));
        if (pix != null && pix.getStatus() == Status.AGENDADO) {
            pix.setStatus(Status.CANCELADO);
            pixRepository.save(pix); 
            queueSender.send("Pix cancelado com sucesso");
            return new PixWrapperDTO<PixResponseDTO>(modelMapper.mapToDTO(pix));
        } else {
            throw new PixNotFoundException("Nenhum pix agendado para cancelar.");
        }
    }

}

