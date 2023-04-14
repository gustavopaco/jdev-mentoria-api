package com.pacoprojects.report;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService serviceReport;

    @PostMapping(path = "relatorioProdutosPorNotaFiscalCompra")
    public ResponseEntity<List<ReportNotaFiscalProjection>> relatorioProdutoPorNotaFiscal(@Valid @RequestBody ReportNotaFiscalDto notaFiscalDto) {
       return ResponseEntity.ok(serviceReport.relatorioProdutoPorNotaFiscal(notaFiscalDto));
    }

    @GetMapping(path = "relatorioProdutoEstoqueBaixo")
    public ResponseEntity<List<ReportProdutoLowStockProjection>> relatorioProdutoEstoqueBaixo(@RequestParam(name = "idEmpresa", required = false) Long idEmpresa) {
        return ResponseEntity.ok(serviceReport.relatorioProdutoEstoqueBaixo(idEmpresa));
    }
}