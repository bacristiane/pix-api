package br.com.pagamentos.pix.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.com.pagamentos.pix.controller.PixController;
import br.com.pagamentos.pix.model.constant.Status;
import br.com.pagamentos.pix.model.dto.PixRequestDTO;
import br.com.pagamentos.pix.model.dto.PixResponseDTO;
import br.com.pagamentos.pix.model.dto.PixWrapperDTO;
import br.com.pagamentos.pix.repository.PixRepository;
import br.com.pagamentos.pix.service.PixService;

@SpringBootTest
@AutoConfigureMockMvc
public class PixControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private PixRepository pixRepository;
    
    @InjectMocks
    private PixService pixService;

    @InjectMocks
    private PixController pixController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(pixController)
                .build();
    }

    @Test
    public void testBuscarPixs() throws Exception {

        mockMvc.perform(get("/api/v1/pix"))
                .andExpect(status().isOk());
                
    }
}
