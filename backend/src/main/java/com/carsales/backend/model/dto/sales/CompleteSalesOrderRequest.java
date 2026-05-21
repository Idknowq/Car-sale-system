package com.carsales.backend.model.dto.sales;

import jakarta.validation.constraints.NotNull;

public class CompleteSalesOrderRequest {
    @NotNull
    private Integer staffId;

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }
}
