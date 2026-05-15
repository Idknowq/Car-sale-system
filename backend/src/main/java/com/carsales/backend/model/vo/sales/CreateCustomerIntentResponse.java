package com.carsales.backend.model.vo.sales;

public class CreateCustomerIntentResponse {
    private Integer customerId;
    private Integer intentId;

    public CreateCustomerIntentResponse() {
    }

    public CreateCustomerIntentResponse(Integer customerId, Integer intentId) {
        this.customerId = customerId;
        this.intentId = intentId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getIntentId() {
        return intentId;
    }

    public void setIntentId(Integer intentId) {
        this.intentId = intentId;
    }
}
