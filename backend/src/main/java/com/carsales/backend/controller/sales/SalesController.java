package com.carsales.backend.controller.sales;

import com.carsales.backend.common.api.ApiResponse;
import com.carsales.backend.model.dto.CreateSalesOrderRequest;
import com.carsales.backend.model.vo.CreateSalesOrderResponse;
import com.carsales.backend.model.vo.SalesOrderVo;
import com.carsales.backend.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sales")
public class SalesController {

    private final OrderService orderService;

    public SalesController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/ping")
    public ApiResponse<String> ping() {
        return ApiResponse.ok("sales-ok");
    }

    @PostMapping("/orders")
    public ApiResponse<CreateSalesOrderResponse> createSalesOrder(@Valid @RequestBody CreateSalesOrderRequest request) {
        return ApiResponse.ok(orderService.createSalesOrder(request));
    }

    @GetMapping("/orders/{orderId}")
    public ApiResponse<SalesOrderVo> getOrderById(@PathVariable Integer orderId) {
        return ApiResponse.ok(orderService.getOrderById(orderId));
    }
}
