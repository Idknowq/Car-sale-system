package com.carsales.backend.controller.inventory;

import com.carsales.backend.common.api.ApiResponse;
import com.carsales.backend.model.dto.inventory.InventoryAlertQueryDto;
import com.carsales.backend.model.dto.inventory.InventoryVehicleQueryDto;
import com.carsales.backend.model.dto.inventory.VehicleInboundRequest;
import com.carsales.backend.model.vo.common.PageResult;
import com.carsales.backend.model.vo.inventory.InventoryAlertVo;
import com.carsales.backend.model.vo.inventory.InventoryVehicleVo;
import com.carsales.backend.model.vo.inventory.VehicleInboundResponse;
import com.carsales.backend.service.inventory.InventoryService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/ping")
    public ApiResponse<String> ping() {
        return ApiResponse.ok("inventory-ok");
    }

    @GetMapping("/vehicles")
    public ApiResponse<PageResult<InventoryVehicleVo>> queryVehicles(@Valid InventoryVehicleQueryDto query) {
        return ApiResponse.ok(inventoryService.queryVehicles(query));
    }

    @PostMapping("/vehicles/inbound")
    public ApiResponse<VehicleInboundResponse> inboundVehicle(@Valid @RequestBody VehicleInboundRequest request) {
        return ApiResponse.ok(inventoryService.inboundVehicle(request));
    }

    @GetMapping("/alerts")
    public ApiResponse<List<InventoryAlertVo>> queryInventoryAlerts(@Valid InventoryAlertQueryDto query) {
        return ApiResponse.ok(inventoryService.queryInventoryAlerts(query));
    }
}
