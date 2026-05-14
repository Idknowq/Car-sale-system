package com.carsales.backend.controller.inventory;

import com.carsales.backend.common.api.ApiResponse;
import com.carsales.backend.model.dto.inventory.InventoryVehicleQueryDto;
import com.carsales.backend.model.vo.common.PageResult;
import com.carsales.backend.model.vo.inventory.InventoryVehicleVo;
import com.carsales.backend.service.inventory.InventoryService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
