package com.migueljuncalp.orderservice.api;

import com.migueljuncalp.orderservice.repository.StockOrderRepository;
import com.migueljuncalp.orderservice.service.StockMovementService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Orders", description = "Reserva de existencia y pedidos")
public class OrdersController {

    private final StockMovementService movementService;
    private final StockOrderRepository stockOrderRepository;

    public OrdersController(StockMovementService movementService, StockOrderRepository stockOrderRepository) {
        this.movementService = movementService;
        this.stockOrderRepository = stockOrderRepository;
    }

    @PostMapping("/stock-order")
    @Operation(summary = "Crear una reserva de stock",
            description = "Persiste el pedido y publica un evento Kafka para procesarlo de forma asíncrona")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Pedido aceptado y publicado"),
            @ApiResponse(responseCode = "400", description = "Petición no válida"),
            @ApiResponse(responseCode = "503", description = "Kafka no está disponible")
    })
    public ResponseEntity<StockOrderResponse> createMovement(
            @Valid @RequestBody CreateOrderRequest request) {
        var response = movementService.create(request);
        return ResponseEntity.accepted()
                .location(URI.create("/api/v1/stock-movements/" + response.orderId()))
                .body(response);
    }

    @GetMapping("/orders/{orderId}")
    @Operation(summary = "Consultar el estado actual de un pedido")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "El producto aún no tiene inventario")
    })
    public StockOrderResponse getInventory(@PathVariable UUID orderId) {
        return stockOrderRepository.findById(orderId)
                .map(StockOrderResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("No existe pedido para " + orderId));
    }

}
