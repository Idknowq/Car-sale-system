package com.carsales.backend.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CreateSalesOrderRequest {
    @NotNull
    private Integer customerId;

    @NotNull
    private Integer staffId;

    @NotBlank
    private String vehicleVin;

    @NotNull
    @DecimalMin(value = "0.00")
    private BigDecimal depositAmount;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal vehicleAmount;

    @DecimalMin(value = "0.00")
    private BigDecimal optionAmount = BigDecimal.ZERO;

    @DecimalMin(value = "0.00")
    private BigDecimal insuranceAmount = BigDecimal.ZERO;

    @DecimalMin(value = "-999999999999.99")
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @DecimalMin(value = "0.00")
    private BigDecimal otherAmount = BigDecimal.ZERO;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public String getVehicleVin() {
        return vehicleVin;
    }

    public void setVehicleVin(String vehicleVin) {
        this.vehicleVin = vehicleVin;
    }

    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    public BigDecimal getVehicleAmount() {
        return vehicleAmount;
    }

    public void setVehicleAmount(BigDecimal vehicleAmount) {
        this.vehicleAmount = vehicleAmount;
    }

    public BigDecimal getOptionAmount() {
        return optionAmount;
    }

    public void setOptionAmount(BigDecimal optionAmount) {
        this.optionAmount = optionAmount;
    }

    public BigDecimal getInsuranceAmount() {
        return insuranceAmount;
    }

    public void setInsuranceAmount(BigDecimal insuranceAmount) {
        this.insuranceAmount = insuranceAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getOtherAmount() {
        return otherAmount;
    }

    public void setOtherAmount(BigDecimal otherAmount) {
        this.otherAmount = otherAmount;
    }
}
