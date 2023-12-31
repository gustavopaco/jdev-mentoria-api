package com.pacoprojects.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pacoprojects.api.integration.melhor.envio.ApiMelhorEnvio;
import com.pacoprojects.api.integration.melhor.envio.MelhorEnvioConsultaFreteDto;
import com.pacoprojects.api.integration.melhor.envio.consulta.frete.request.RequestMelhorEnvioConsultaFreteDto;
import com.pacoprojects.api.integration.melhor.envio.imprimir.etiqueta.response.ResponseMelhorEnvioImprimirEtiquetaDto;
import com.pacoprojects.api.integration.melhor.envio.rastreio.pedido.response.ResponseMelhorEnvioRastreioPedidoDto;
import com.pacoprojects.dto.VendaCompraDto;
import com.pacoprojects.dto.projections.ItemVendaCompraSelected;
import com.pacoprojects.dto.projections.VendaCompraProjectionSelected;
import com.pacoprojects.service.VendaCompraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "vendaCompra")
@RequiredArgsConstructor
public class VendaCompraController {

    private final VendaCompraService serviceVendaCompra;
    private final ApiMelhorEnvio apiMelhorEnvio;

    @GetMapping
    public ResponseEntity<List<VendaCompraProjectionSelected>> getAllVendaCompra(@RequestParam(name = "idEmpresa") Long idEmpresa) {
        return ResponseEntity.ok(serviceVendaCompra.getAllVendaCompra(idEmpresa));
    }

    @GetMapping(path = "consultaVenda")
    public ResponseEntity<List<ItemVendaCompraSelected>> getAllVendaCompraByParam(@RequestParam(name = "idCliente", required = false)
                                                                                  Long idCliente,
                                                                                  @RequestParam(name = "idProduto", required = false)
                                                                                  Long idProduto,
                                                                                  @RequestParam(name = "nomeProduto", required = false)
                                                                                  String nomeProduto,
                                                                                  @RequestParam(name = "nomeCliente", required = false)
                                                                                  String nomeCliente,
                                                                                  @RequestParam(name = "endCobranca", required = false)
                                                                                  String endCobranca,
                                                                                  @RequestParam(name = "endEntrega", required = false)
                                                                                  String endEntrega,
                                                                                  @RequestParam(name = "cpf", required = false) String cpf) {
        return ResponseEntity.ok(serviceVendaCompra.getAllVendaCompraByParam(idCliente, idProduto, nomeProduto, nomeCliente, endCobranca, endEntrega, cpf));
    }

    @GetMapping(path = "consultaVendaFaixaDatas")
    public ResponseEntity<List<ItemVendaCompraSelected>> getAllVendaCompraByIntervalDates(@RequestParam(name = "dataInicial")
                                                                                          @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
                                                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd")
                                                                                          LocalDate dataInicial,

                                                                                          @RequestParam(name = "dataFinal")
                                                                                          @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
                                                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd")
                                                                                          LocalDate dataFinal) {
        return ResponseEntity.ok(serviceVendaCompra.getAllVendaCompraByIntervalDates(dataInicial, dataFinal));
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<VendaCompraProjectionSelected> getVendaCompraById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(serviceVendaCompra.getVendaCompraById(id));
    }

    @GetMapping(path = "imprimeMelhorEnvioEtiqueta")
    public ResponseEntity<ResponseMelhorEnvioImprimirEtiquetaDto> imprimeMelhorEnvioEtiqueta(@RequestParam(name = "idVenda") Long idVenda) {
        return ResponseEntity.ok(apiMelhorEnvio.apiImprimeEtiquetaMelhorEnvio(idVenda));
    }

    @PostMapping(path = "consultaMelhorEnvioFrete")
    public ResponseEntity<List<MelhorEnvioConsultaFreteDto>> consultaMelhorEnvioFrete(@RequestBody RequestMelhorEnvioConsultaFreteDto requestMelhorEnvioConsultaFreteDto) {
        return ResponseEntity.ok(apiMelhorEnvio.apiConsultaFreteMelhorEnvio(requestMelhorEnvioConsultaFreteDto));
    }

    @PostMapping(path = "cancelarEtiqueta")
    public void cancelarEtiqueta(@RequestParam(name = "idVenda") Long idVenda) {
        apiMelhorEnvio.apiCancelarEtiquetaMelhorEnvio(idVenda);
    }

    @PostMapping(path = "rastreioPedido")
    public ResponseEntity<ResponseMelhorEnvioRastreioPedidoDto> rastreioPedido(@RequestParam(name = "idVenda") Long idVenda) {
        return ResponseEntity.ok(apiMelhorEnvio.apiConsultaRastreioPedidoMelhorEnvio(idVenda));
    }

    @PostMapping
    public ResponseEntity<VendaCompraDto> addVendaCompra(@Valid @RequestBody VendaCompraDto vendaCompraDto) {
        return ResponseEntity.ok(serviceVendaCompra.addVendaCompra(vendaCompraDto));
    }

    @PutMapping
    public void enableVendaCompra(@RequestParam(name = "idVenda") Long idVenda) {
        serviceVendaCompra.enableVendaCompra(idVenda);
    }

    @DeleteMapping(path = "{id}")
    public void deleteVendaCompra(@PathVariable(name = "id") Long id) {
        serviceVendaCompra.deleteVendaCompra(id);
    }
}
