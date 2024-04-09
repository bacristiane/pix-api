package br.com.pagamentos.pix.controller;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import br.com.pagamentos.pix.service.PixService;
import br.com.pagamentos.pix.model.constant.Status;
import br.com.pagamentos.pix.model.dto.PixRequestDTO;
import br.com.pagamentos.pix.model.dto.PixResponseDTO;
import br.com.pagamentos.pix.model.dto.PixWrapperDTO;

@RestController
@RequestMapping("api/v1/pix")
@Validated
public class PixController {

    @Autowired
    private PixService pixService;

    @PostMapping()
    public ResponseEntity<PixWrapperDTO<PixResponseDTO>> criarPix(@RequestBody @Valid PixRequestDTO pixDTO) {
        PixWrapperDTO<PixResponseDTO> response = pixService.criarPix(pixDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping()
public ResponseEntity<PixWrapperDTO<List<PixResponseDTO>>> buscarPixs(@RequestParam(value = "status", required = false) Status status) {
    PixWrapperDTO<List<PixResponseDTO>> response = pixService.buscarPixs(status);
    return ResponseEntity.ok(response);
}

    @GetMapping("/{id}")
    public ResponseEntity<PixWrapperDTO<PixResponseDTO>> buscarPix(@PathVariable Long id) {
        PixWrapperDTO<PixResponseDTO>response = pixService.buscarPix(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PixWrapperDTO<PixResponseDTO>> atualizarPix(@PathVariable Long id, @RequestBody @Valid  PixRequestDTO PixDTO) {
        PixWrapperDTO<PixResponseDTO> response = pixService.atualizarPix(id, PixDTO);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PixWrapperDTO<PixResponseDTO>> atualizarPixParcialmente(@PathVariable Long id, @RequestBody @Valid  PixRequestDTO PixDTO) {
        PixWrapperDTO<PixResponseDTO> response = pixService.atualizarPix(id, PixDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public void deletarPix(@PathVariable Long id) {
        
            pixService.deletarPix(id);
            
    }


}
