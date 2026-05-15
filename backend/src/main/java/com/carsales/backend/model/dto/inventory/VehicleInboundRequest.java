package com.carsales.backend.model.dto.inventory;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class VehicleInboundRequest {

    @NotBlank(message = "vehicleVin is required")
    @Size(min = 17, max = 17, message = "vehicleVin length must be 17")
    private String vehicleVin;

    @NotNull(message = "staffId is required")
    private Integer staffId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate inventoryInDate;

    @Size(max = 200, message = "reason length must be <= 200")
    private String reason;

    public String getVehicleVin() {
        return vehicleVin;
    }

    public void setVehicleVin(String vehicleVin) {
        this.vehicleVin = vehicleVin;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public LocalDate getInventoryInDate() {
        return inventoryInDate;
    }

    public void setInventoryInDate(LocalDate inventoryInDate) {
        this.inventoryInDate = inventoryInDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
