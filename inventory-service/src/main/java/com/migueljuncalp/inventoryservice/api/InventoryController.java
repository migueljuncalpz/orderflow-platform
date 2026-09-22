package com.migueljuncalp.inventoryservice.api;

import com.migueljuncalp.inventoryservice.repository.InventoryRepository;
import com.migueljuncalp.inventoryservice.service.StockMovementService;
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
@Tag(name = "Inventory", description = "Movimientos y consulta de existencias")
public class InventoryController {

    private final StockMovementService movementService;
    private final InventoryRepository inventoryRepository;

    public InventoryController(StockMovementService movementService, InventoryRepository inventoryRepository) {
        this.movementService = movementService;
        this.inventoryRepository = inventoryRepository;
    }

    @PostMapping("/stock-movements")
    @Operation(summary = "Crear un movimiento de stock",
            description = "Persiste el movimiento y publica un evento Kafka para procesarlo de forma asíncrona")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Movimiento aceptado y publicado"),
            @ApiResponse(responseCode = "400", description = "Petición no válida"),
            @ApiResponse(responseCode = "503", description = "Kafka no está disponible")
    })
    public ResponseEntity<StockMovementResponse> createMovement(
            @Valid @RequestBody CreateStockMovementRequest request) {
        var response = movementService.create(request);
        return ResponseEntity.accepted()
                .location(URI.create("/api/v1/stock-movements/" + response.id()))
                .body(response);
    }

    @GetMapping("/inventory/{productId}")
    @Operation(summary = "Consultar el stock actual de un producto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Inventario encontrado"),
            @ApiResponse(responseCode = "404", description = "El producto aún no tiene inventario")
    })
    public InventoryResponse getInventory(@PathVariable String productId) {
        return inventoryRepository.findById(productId)
                .map(InventoryResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("No existe inventario para " + productId));
    }

    @GetMapping("/stock-movements/{id}")
    @Operation(summary = "Consultar un movimiento por su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimiento encontrado"),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado")
    })
    public StockMovementResponse getMovement(@PathVariable UUID id) {
        return movementService.findById(id);
    }
}
