package br.com.pagamentos.pix.controller;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import br.com.pagamentos.pix.service.PixService;
import br.com.pagamentos.pix.model.PixDTO;
import br.com.pagamentos.pix.model.StatusPix;

@RestController
@RequestMapping("api/v1/pix")
public class PixController {

    @Autowired
    private PixService pixService;

    @PostMapping
    public ResponseEntity<PixDTO> criarPix(@RequestBody PixDTO PixDTO) {
        PixDTO novoPixDTO = pixService.criarPix(PixDTO);
        return new ResponseEntity<>(novoPixDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PixDTO>> buscarPixs(@RequestParam(value = "status", required = false) StatusPix status) {
        List<PixDTO> pixList = pixService.buscarPixs(status);
        return ResponseEntity.ok(pixList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PixDTO> buscarPix(@PathVariable Long id) {
        PixDTO PixDTO = pixService.buscarPix(id);
        return ResponseEntity.ok(PixDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PixDTO> atualizarPix(@PathVariable Long id, @RequestBody PixDTO PixDTO) {
        PixDTO PixAtualizadoDTO = pixService.atualizarPix(id, PixDTO);
        return ResponseEntity.ok(PixAtualizadoDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarPix(@PathVariable Long id) {
        pixService.deletarPix(id);
        return ResponseEntity.ok("Test");
    }


}
