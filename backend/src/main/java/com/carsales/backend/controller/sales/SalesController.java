package com.carsales.backend.controller.sales;

import com.carsales.backend.common.api.ApiResponse;
import com.carsales.backend.model.dto.sales.CreateCustomerIntentRequest;
import com.carsales.backend.model.dto.sales.CreateSalesOrderRequest;
import com.carsales.backend.model.dto.sales.CompleteSalesOrderRequest;
import com.carsales.backend.model.dto.sales.MyOrderQueryRequest;
import com.carsales.backend.model.vo.sales.CreateCustomerIntentResponse;
import com.carsales.backend.model.vo.sales.CreateSalesOrderResponse;
import com.carsales.backend.model.vo.sales.CompleteSalesOrderResponse;
import com.carsales.backend.model.vo.sales.MyOrderItemVo;
import com.carsales.backend.model.vo.common.PageResult;
import com.carsales.backend.model.vo.sales.SalesOrderVo;
import com.carsales.backend.service.sales.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    @PostMapping("/orders/{orderId}/complete")
    public ApiResponse<CompleteSalesOrderResponse> completeSalesOrder(
            @PathVariable Integer orderId,
            @Valid @RequestBody CompleteSalesOrderRequest request
    ) {
        return ApiResponse.ok(orderService.completeSalesOrder(orderId, request));
    }

    @GetMapping("/orders/{orderId}")
    public ApiResponse<SalesOrderVo> getOrderById(@PathVariable Integer orderId) {
        return ApiResponse.ok(orderService.getOrderById(orderId));
    }

    @GetMapping("/orders/mine")
    public ApiResponse<PageResult<MyOrderItemVo>> queryMyOrders(@ModelAttribute MyOrderQueryRequest request) {
        return ApiResponse.ok(orderService.queryMyOrders(request));
    }

    @PostMapping("/intents")
    public ApiResponse<CreateCustomerIntentResponse> createCustomerIntent(
            @Valid @RequestBody CreateCustomerIntentRequest request
    ) {
        return ApiResponse.ok(orderService.createCustomerIntent(request));
    }
}
