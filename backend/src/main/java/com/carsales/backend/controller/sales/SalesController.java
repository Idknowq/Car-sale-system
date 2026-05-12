package com.carsales.backend.controller.sales;

import com.carsales.backend.common.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sales")
public class SalesController {
    @GetMapping("/ping")
    public ApiResponse<String> ping() { return ApiResponse.ok("sales-ok"); }
}
