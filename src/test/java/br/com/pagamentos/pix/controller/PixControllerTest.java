package br.com.pagamentos.pix.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.time.LocalDate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import br.com.pagamentos.pix.model.Destino;
import br.com.pagamentos.pix.model.Pix;
import br.com.pagamentos.pix.model.RecorrenciaPix;
import br.com.pagamentos.pix.model.constant.FrequenciaRecorrencia;
import br.com.pagamentos.pix.model.constant.Status;
import br.com.pagamentos.pix.model.constant.TipoChave;
import br.com.pagamentos.pix.model.dto.PixRequestDTO;
import br.com.pagamentos.pix.repository.PixRepository;
import br.com.pagamentos.pix.service.PixService;
import java.util.Optional;



@SpringBootTest
@AutoConfigureMockMvc
public class PixControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PixService pixService;

    @MockBean
    private PixRepository pixRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }


    @Test
    public void whenCallCriarPix_shouldReturnStatus201() throws Exception {
     
        PixRequestDTO mockRequestDto = requestDTO();
        Pix mockPix = mockPix();


       when(pixRepository.save(any())).thenReturn(mockPix);
        
       String jsonRequest = objectMapper.writeValueAsString(mockRequestDto);


        mockMvc.perform(post("/api/v1/pix")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated());
            
    }

    @Test
    public void whenCallBuscarPixs_shouldReturnStatus200() throws Exception {
        Pix mockPix = mockPix();

        when(pixRepository.findAll()).thenReturn(List.of(mockPix));
        when(pixRepository.findByStatus(Status.AGENDADO)).thenReturn(List.of(mockPix));
        

        mockMvc.perform(get("/api/v1/pix")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/pix").queryParam("status", "AGENDADO")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
                
    }

    @Test
    public void whenCallBuscarPix_shouldReturnStatus200() throws Exception {
        Pix mockPix = mockPix();


        when(pixRepository.findById(1L)).thenReturn(Optional.of(mockPix));

        mockMvc.perform(get("/api/v1/pix/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
                
    }

    

    @Test
    public void whenCallAtualizarPix_shouldReturnStatus200() throws Exception {
        PixRequestDTO mockRequestDto = requestDTO();
        Pix mockPix = mockPix();
        mockRequestDto.setValor(200.0);
        mockPix.setStatus(Status.AGENDADO);

        when(pixRepository.findById(1L)).thenReturn(Optional.of(mockPix));
        when(pixRepository.save(any())).thenReturn(mockPix);
        
       String jsonRequest = objectMapper.writeValueAsString(mockRequestDto);

                
                mockMvc.perform(put("/api/v1/pix/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    public void whenCallAtualizarPixParcialmente_shouldReturnStatus200() throws Exception {
        PixRequestDTO mockRequestDto = requestDTO();
        Pix mockPix = mockPix();
        mockRequestDto.setValor(200.0);
        mockPix.setStatus(Status.AGENDADO);

        when(pixRepository.findById(1L)).thenReturn(Optional.of(mockPix));
        when(pixRepository.save(any())).thenReturn(mockPix);
        
       String jsonRequest = objectMapper.writeValueAsString(mockRequestDto);

                
                mockMvc.perform(patch("/api/v1/pix/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    public void whenCallDeletarPix_shouldReturnStatus200() throws Exception {
        Pix mockPix = mockPix();
        mockPix.setStatus(Status.AGENDADO);


        when(pixRepository.findById(1L)).thenReturn(Optional.of(mockPix));
        when(pixRepository.save(any())).thenReturn(mockPix);

        mockMvc.perform(delete("/api/v1/pix/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
                
    }

    @Test
    public void whenCallCriarPix_shouldReturnStatus400IfInputIsInvalid() throws Exception {
        PixRequestDTO mockRequestDto = requestDTO();
        Pix mockPix = mockPix();
        mockRequestDto.setDataPagamento(LocalDate.now().minusDays(1));
        mockRequestDto.setValor(-100.0);
        mockRequestDto.setChavePix("kdaljdaljdf-0");


        when(pixRepository.save(any())).thenReturn(mockPix);

        String jsonRequest = objectMapper.writeValueAsString(mockRequestDto);

        // Teste para data de pagamento no passado
        mockMvc.perform(post("/api/v1/pix")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.dataPagamento").value("A data de pagamento deve ser no presente ou futuro"))
                .andExpect(jsonPath("$.chavePix").value("A chave pix deve ser um CPF, e-mail, telefone ou UUID"))
                .andExpect(jsonPath("$.valor").value("O valor do pagamento deve ser positivo"));
                
    }


    @Test
    public void whenCallBuscarPix_shouldReturnStatus404IfPixNotFound() throws Exception {
        Pix mockPix = mockPix();


        when(pixRepository.findById(1L)).thenReturn(Optional.of(mockPix));
        

        mockMvc.perform(get("/api/v1/pix/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Pix não encontrado"));
                
    }

    @Test
    public void whenCallAtualizarPix_shouldReturnStatus404IfStatusPixIsNotAgendadoOrPixNotFound() throws Exception {
        PixRequestDTO mockRequestDto = requestDTO();
        Pix mockPix = mockPix();
        
        

        when(pixRepository.findById(1L)).thenReturn(Optional.of(mockPix));
        when(pixRepository.save(any())).thenReturn(mockPix);

        String jsonRequest = objectMapper.writeValueAsString(mockRequestDto);

        // Teste para status diferente de agendado
        mockMvc.perform(put("/api/v1/pix/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Nenhum pix agendado para atualizar."));
       // Teste para pix não encontrado
        mockMvc.perform(put("/api/v1/pix/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Pix não encontrado."));
                
                
    }

    @Test
    public void whenCallDeletarPix_shouldReturnStatus404IfStatusPixIsNotAgendadoOrPixNotFound() throws Exception {
        Pix mockPix = mockPix();
        

        when(pixRepository.findById(1L)).thenReturn(Optional.of(mockPix));
        when(pixRepository.save(any())).thenReturn(mockPix);
        // Teste para status diferente de agendado
        mockMvc.perform(delete("/api/v1/pix/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Nenhum pix agendado para cancelar."));
        // Teste para pix não encontrado
        mockMvc.perform(delete("/api/v1/pix/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Pix não encontrado."));
                
                
    }

    
    
    

    private Pix mockPix(){

        Destino destino = new Destino();
    destino.setChavePix("email@email.com");
    destino.setTipoChavePix(TipoChave.EMAIL);
    RecorrenciaPix recorrencia = new RecorrenciaPix();
    recorrencia.setDataFinal(LocalDate.now().plusMonths(1));
    recorrencia.setFrequencia(FrequenciaRecorrencia.MENSAL);
        Pix mockPix = new Pix();
        mockPix.setId(1L);
        mockPix.setDataInclusao(LocalDate.now());
        mockPix.setDataPagamento(LocalDate.now());
        mockPix.setDescricao("Teste");
        mockPix.setValor(100.0);
        mockPix.setStatus(Status.EFETUADO);
        mockPix.setDestinoPix(destino);
        mockPix.setRecorrencia(recorrencia);

        return mockPix;

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





}

    

