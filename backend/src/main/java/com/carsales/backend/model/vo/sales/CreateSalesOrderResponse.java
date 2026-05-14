package com.carsales.backend.model.vo.sales;

public class CreateSalesOrderResponse {
    private Integer orderId;

    public CreateSalesOrderResponse() {
    }

    public CreateSalesOrderResponse(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
