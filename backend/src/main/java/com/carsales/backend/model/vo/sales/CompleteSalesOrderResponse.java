package com.carsales.backend.model.vo.sales;

public class CompleteSalesOrderResponse {
    private Integer orderId;
    private String orderStatus;

    public CompleteSalesOrderResponse() {
    }

    public CompleteSalesOrderResponse(Integer orderId, String orderStatus) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
