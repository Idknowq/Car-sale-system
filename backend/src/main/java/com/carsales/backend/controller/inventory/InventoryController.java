package com.carsales.backend.controller.inventory;

import com.carsales.backend.common.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    @GetMapping("/ping")
    public ApiResponse<String> ping() { return ApiResponse.ok("inventory-ok"); }
}
