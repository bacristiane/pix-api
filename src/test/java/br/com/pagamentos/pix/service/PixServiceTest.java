package br.com.pagamentos.pix.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.pagamentos.pix.messaging.QueueSender;
import br.com.pagamentos.pix.model.Destino;
import br.com.pagamentos.pix.model.Pix;
import br.com.pagamentos.pix.model.RecorrenciaPix;
import br.com.pagamentos.pix.model.constant.FrequenciaRecorrencia;
import br.com.pagamentos.pix.model.constant.Status;
import br.com.pagamentos.pix.model.constant.TipoChave;
import br.com.pagamentos.pix.model.dto.PixRequestDTO;
import br.com.pagamentos.pix.model.dto.PixResponseDTO;
import br.com.pagamentos.pix.model.dto.PixWrapperDTO;
import br.com.pagamentos.pix.model.mapper.PixMapper;
import br.com.pagamentos.pix.repository.PixRepository;
import br.com.pagamentos.pix.utils.PixUtils;



public class PixServiceTest {

    @Mock
    private PixRepository pixRepository;

    @Mock
    private PixMapper pixMapper;

    @Mock
    private PixUtils pixUtils;

    @Mock
    private QueueSender queueSender;

    @InjectMocks
    private PixService pixService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenCallCriarPix_shouldReturnPixWrapperDTOWithoutMessage() {

        // Mock data
        PixRequestDTO requestDTO = requestDTO();
        Pix pix = pix(requestDTO);
        PixResponseDTO responseDTO = responseDTO(pix);
        PixWrapperDTO<PixResponseDTO> expectedResponse = new PixWrapperDTO<>(responseDTO);

        // Mock behavior
        
        when(pixRepository.existsByValorAndDataPagamentoAndDestinoPix_ChavePix(requestDTO.getValor(), requestDTO.getDataPagamento(), requestDTO.getChavePix())).thenReturn(false);
        when(pixMapper.mapToCreateEntity(requestDTO)).thenReturn(pix);
        when(pixRepository.save(pix)).thenReturn(pix);
        when(pixMapper.mapToDTO(pix)).thenReturn(responseDTO);

        // Test
        PixWrapperDTO<PixResponseDTO> result = pixService.criarPix(requestDTO);

        // Assertions
        assertEquals(expectedResponse, result);
        verify(queueSender, times(1)).send(anyString());
    }


    @Test
    public void whenCallCriarPix_shouldReturnPixWrapperDTOWithtMessage() {

        // Mock data
        PixRequestDTO requestDTO = requestDTO();
        Pix pix = pix(requestDTO);
        PixResponseDTO responseDTO = responseDTO(pix);
        PixWrapperDTO<PixResponseDTO> expectedResponse = new PixWrapperDTO<>(responseDTO, "AVISO:Já existe um Pix com o mesmo valor, data e destino.");

        // Mock behavior
        when(pixRepository.existsByValorAndDataPagamentoAndDestinoPix_ChavePix(requestDTO.getValor(), requestDTO.getDataPagamento(), requestDTO.getChavePix())).thenReturn(true);
        when(pixMapper.mapToCreateEntity(requestDTO)).thenReturn(pix);
        when(pixRepository.save(pix)).thenReturn(pix);
        when(pixMapper.mapToDTO(pix)).thenReturn(responseDTO);
      
        // Test
        PixWrapperDTO<PixResponseDTO> result = pixService.criarPix(requestDTO);

        // Assertions
        assertEquals(expectedResponse, result);
        verify(queueSender, times(1)).send(anyString());
    }

    @Test
    public void whenCallBuscarPix_shouldReturnPixWrapperDTOWithtPix() {
        // Mock data
        Long id = 1L;
        Pix pix = new Pix();
        PixResponseDTO responseDTO = new PixResponseDTO();
        PixWrapperDTO<PixResponseDTO> expectedResponse = new PixWrapperDTO<>(responseDTO);

        // Mock behavior
        when(pixRepository.findById(id)).thenReturn(Optional.of(pix));
        when(pixMapper.mapToDTO(pix)).thenReturn(responseDTO);

        // Test
        PixWrapperDTO<PixResponseDTO> result = pixService.buscarPix(id);

        // Assertions
        assertEquals(expectedResponse, result);
    }

    @Test
    public void whenCallBuscarPixs_shouldReturnPixWrapperDTOWithtListOfPixs() {
        // Mock data
        Status status = Status.CANCELADO;
        List<Pix> pixList = new ArrayList<>();
        PixResponseDTO responseDTO = new PixResponseDTO();
        List<PixResponseDTO> responseList = new ArrayList<>();
        responseList.add(responseDTO);
        PixWrapperDTO<List<PixResponseDTO>> expectedResponse = new PixWrapperDTO<>(responseList);

        // Mock behavior
        when(pixRepository.findByStatus(status)).thenReturn(pixList);
        when(pixMapper.mapPixListToDTOs(pixList)).thenReturn(responseList);

        // Test
        PixWrapperDTO<List<PixResponseDTO>> result = pixService.buscarPixs(status);

        // Assertions
        assertEquals(expectedResponse, result);
    }

    @Test
    public void whenCallAtualizarPix_shouldReturnPixWrapperDTOWithtPix() {
        // Mock data
        Long id = 1L;
        PixRequestDTO requestDTO = requestDTO();
        requestDTO.setDataPagamento(LocalDate.now());
        Pix pix = pix(requestDTO);
        Destino destino = new Destino();
        destino.setTipoChavePix(TipoChave.EMAIL);
        pix.setDestinoPix(destino);
        pix.setStatus(Status.AGENDADO);
        PixResponseDTO responseDTO = responseDTO(pix);
        PixWrapperDTO<PixResponseDTO> expectedResponse = new PixWrapperDTO<>(responseDTO);
  

        // Mock behavior
       
        when(pixRepository.findById(id)).thenReturn(Optional.of(pix));
        when(pixUtils.definirStatus(requestDTO.getDataPagamento())).thenReturn(Status.EFETUADO);
        when(pixMapper.mapToUpdateEntity(requestDTO, pix)).thenReturn(pix);
        when(pixUtils.verificarTipoChavePix(requestDTO.getChavePix())).thenReturn(TipoChave.EMAIL);
        when(pixMapper.mapToCreateEntity(requestDTO)).thenReturn(pix);
        when(pixRepository.save(pix)).thenReturn(pix);
        when(pixMapper.mapToDTO(pix)).thenReturn(responseDTO);

        // Test
        PixWrapperDTO<PixResponseDTO> result = pixService.atualizarPix(id, requestDTO);

        // Assertions
        assertEquals(expectedResponse, result);
    }

    @Test
    public void whenCallDeletarPix_shouldBeVoid() {
        // Mock data
        Status status = Status.CANCELADO;
        List<Pix> pixList = new ArrayList<>();
        PixResponseDTO responseDTO = new PixResponseDTO();
        List<PixResponseDTO> responseList = new ArrayList<>();
        responseList.add(responseDTO);
        PixWrapperDTO<List<PixResponseDTO>> expectedResponse = new PixWrapperDTO<>(responseList);

        // Mock behavior
        when(pixRepository.findByStatus(status)).thenReturn(pixList);
        when(pixMapper.mapPixListToDTOs(pixList)).thenReturn(responseList);

        // Test
        PixWrapperDTO<List<PixResponseDTO>> result = pixService.buscarPixs(status);

        // Assertions
        assertEquals(expectedResponse, result);
    }
private PixRequestDTO requestDTO(){

    PixRequestDTO requestDTO = new PixRequestDTO();
    requestDTO.setValor(100.0);
    requestDTO.setDataPagamento(LocalDate.now());
    requestDTO.setChavePix("email@email.com");
    requestDTO.setDescricao("pagamento pix");
    requestDTO.setDataFinal(LocalDate.now().plusDays(30));
    requestDTO.setFrequencia(FrequenciaRecorrencia.MENSAL);
    return requestDTO;
        
};

private Pix pix(PixRequestDTO requestDTO){

    Destino destino = new Destino();
    destino.setChavePix(requestDTO.getChavePix());
    destino.setTipoChavePix(TipoChave.EMAIL);
    RecorrenciaPix recorrencia = new RecorrenciaPix();
    recorrencia.setDataFinal(requestDTO.getDataFinal());
    recorrencia.setFrequencia(requestDTO.getFrequencia());

    Pix pix = new Pix();
    pix.setDataPagamento(requestDTO.getDataPagamento());
    pix.setValor(requestDTO.getValor());
    pix.setDescricao(requestDTO.getDescricao());
    pix.setRecorrencia(recorrencia);
    pix.setDestinoPix(destino);
    return pix;

   
}


private PixResponseDTO responseDTO(Pix pix){

    PixResponseDTO responseDTO = new PixResponseDTO();
    responseDTO.setValor(pix.getValor());
    responseDTO.setDataPagamento(pix.getDataPagamento());
    responseDTO.setStatus(pix.getStatus());
    responseDTO.setDataInclusao(pix.getDataInclusao());
    responseDTO.setChavePix(pix.getDestinoPix().getChavePix());
    return responseDTO;
    
}

}

