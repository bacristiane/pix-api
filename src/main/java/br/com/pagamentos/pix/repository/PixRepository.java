package br.com.pagamentos.pix.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import br.com.pagamentos.pix.model.Pix;
import br.com.pagamentos.pix.model.StatusPix;
import java.time.LocalDate;

public interface PixRepository extends JpaRepository<Pix, Long> {

    List<Pix> findByStatus(StatusPix status);
    boolean existsByValorAndDataPagamentoAndDestinoPix_ChavePix(double valor, LocalDate dataPagamento, String chavePix);
}