package br.com.pagamentos.pix.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDate;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.pagamentos.pix.components.QueueSender;
import br.com.pagamentos.pix.model.Pix;
import br.com.pagamentos.pix.model.PixDTO;
import br.com.pagamentos.pix.model.StatusPix;
import br.com.pagamentos.pix.repository.PixRepository;

@Service
public class PixService {

    private static final Logger logger = LoggerFactory.getLogger(PixService.class);


    @Autowired
    private PixRepository pixRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private QueueSender queueSender;

   
    public PixDTO criarPix(PixDTO pixDTO) {

        // Verificar se a data do pagamento é futura ou atual
        if (pixDTO.getDataPagamento().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A data do pagamento não pode ser no passado.");
        }

        // verificar o tipo de chave pix e colocar qual é na propriedade tipo de chave

        // Verificar se já existe um Pix com as mesmas condições
        boolean pixExistente = pixRepository.existsByValorAndDataPagamentoAndDestinoPix_ChavePix(
                pixDTO.getValor(), pixDTO.getDataPagamento(), pixDTO.getChavePix());

                //criar variavel mensagem

                if (pixExistente) {
                    logger.warn("Já existe um Pix com o mesmo valor, data e destino.");
                }

        // Definir o status do Pix com base na data de pagamento
        StatusPix status;
        if (pixDTO.getDataPagamento().isEqual(LocalDate.now())) {
            status = StatusPix.EFETUADO;
        } else {
            status = StatusPix.AGENDADO;
        }

        // // Criar o objeto Pix a ser salvo no banco de dados
        // Pix pix = new Pix();
        // pix.setValor(pixDTO.getValor());
        // pix.setDataPagamento(pixDTO.getDataPagamento());
        // pix.setDestinoPix()
        // pix.setStatus(status);
        // pix.setRecorrencia(pixDTO.getRecorrencia().orElse(null));

        // Salvar o Pix no banco de dados
        

        Pix pix = modelMapper.map(pixDTO, Pix.class);
        pix = pixRepository.save(pix);
        queueSender.send("test message");
        return modelMapper.map(pix, PixDTO.class);
    }
    
    public PixDTO buscarPix(Long id) {
        Pix pix = pixRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pix não encontrado"));
        return modelMapper.map(pix, PixDTO.class);
    }

    public List<PixDTO> buscarPixs(StatusPix status) {
    List<Pix> pixList;

    if (status != null)
        pixList = pixRepository.findByStatus(status);
    // if ("EFETUADO".equals(status)) {
    //     pixList = pixRepository.findByStatus(StatusPix.EFETUADO);
    // } else if ("AGENDADO".equals(status)) {
    //     pixList = pixRepository.findByStatus(StatusPix.AGENDADO);
    else {
        pixList = pixRepository.findAll();
    }
    return pixList.stream()
    .map(pix -> modelMapper.map(pix, PixDTO.class))
    .collect(Collectors.toList());
}


    public PixDTO atualizarPix(Long id, PixDTO pixDTO) {
        // Implementar a lógica para atualizar um Pix
        return null;
    }

    public void deletarPix(Long id) {
        // Implementar a lógica para deletar um Pix
    }

}

