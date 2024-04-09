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
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import br.com.pagamentos.pix.components.QueueSender;
import br.com.pagamentos.pix.model.Destino;
import br.com.pagamentos.pix.model.Pix;
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
    public void testCriarPix() {
        // Mock data
        PixRequestDTO requestDTO = new PixRequestDTO();
        requestDTO.setValor(100.0);
        requestDTO.setDataPagamento(LocalDate.now());
        requestDTO.setChavePix("barbara@email.com");
        requestDTO.setDescricao("Pagamento de aluguel");
        requestDTO.setDataFinal(LocalDate.now().plusDays(30));
        requestDTO.setFrequencia(FrequenciaRecorrencia.MENSAL);
        
        Pix pix = new Pix();

        pix.setValor(requestDTO.getValor());
        pix.setDataPagamento(requestDTO.getDataPagamento());
        pix.setStatus(Status.EFETUADO);
        pix.setDataInclusao(LocalDate.now());
        pix.setDescricao(requestDTO.getDescricao());
        pix.setRecorrencia(pixMapper.mapToRecorrencia(requestDTO));
        pix.setDestinoPix(pixMapper.mapToDestino(requestDTO));
        
        

        PixResponseDTO responseDTO = new PixResponseDTO();

        responseDTO.setValor(pix.getValor());
        responseDTO.setDataPagamento(pix.getDataPagamento());
        responseDTO.setStatus(pix.getStatus());
        responseDTO.setDataInclusao(pix.getDataInclusao());
        responseDTO.setChavePix(requestDTO.getChavePix());


        PixWrapperDTO<PixResponseDTO> expectedResponse = new PixWrapperDTO<>(responseDTO);

        // Mock behavior
        when(pixMapper.mapToCreateEntity(requestDTO)).thenReturn(pix);
        when(pixRepository.existsByValorAndDataPagamentoAndDestinoPix_ChavePix(requestDTO.getValor(), requestDTO.getDataPagamento(), requestDTO.getChavePix())).thenReturn(false);
        when(pixUtils.verificarTipoChavePix(requestDTO.getChavePix())).thenReturn(TipoChave.EMAIL);
        when(pixRepository.save(pix)).thenReturn(pix);
        when(pixMapper.mapToDTO(pix)).thenReturn(responseDTO);
        when(pixRepository.save(pix)).thenReturn(pix);
        when(pixMapper.mapToDTO(pix)).thenReturn(responseDTO);

        // Test
        PixWrapperDTO<PixResponseDTO> result = pixService.criarPix(requestDTO);

        // Assertions
        assertEquals(expectedResponse, result);
        verify(queueSender, times(1)).send(anyString());
    }

    @Test
    public void testBuscarPix() {
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
    public void testBuscarPixs() {
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
    public void testAtualizarPix() {
        // Mock data
        

        Long id = 1L;
        PixRequestDTO requestDTO = new PixRequestDTO();
        requestDTO.setDataPagamento(LocalDate.now());
        Pix pix = new Pix();
        pix.setDestinoPix(pixMapper.mapToDestino(requestDTO));

        pix.setStatus(Status.AGENDADO);
        PixResponseDTO responseDTO = new PixResponseDTO();
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


    
}

