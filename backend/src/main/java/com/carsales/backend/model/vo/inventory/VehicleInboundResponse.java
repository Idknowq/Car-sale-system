package com.carsales.backend.model.vo.inventory;

import java.time.LocalDate;

public class VehicleInboundResponse {

    private String vehicleVin;
    private String fromStatus;
    private String toStatus;
    private LocalDate inventoryInDate;

    public VehicleInboundResponse(String vehicleVin, String fromStatus, String toStatus, LocalDate inventoryInDate) {
        this.vehicleVin = vehicleVin;
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
        this.inventoryInDate = inventoryInDate;
    }

    public String getVehicleVin() {
        return vehicleVin;
    }

    public void setVehicleVin(String vehicleVin) {
        this.vehicleVin = vehicleVin;
    }

    public String getFromStatus() {
        return fromStatus;
    }

    public void setFromStatus(String fromStatus) {
        this.fromStatus = fromStatus;
    }

    public String getToStatus() {
        return toStatus;
    }

    public void setToStatus(String toStatus) {
        this.toStatus = toStatus;
    }

    public LocalDate getInventoryInDate() {
        return inventoryInDate;
    }

    public void setInventoryInDate(LocalDate inventoryInDate) {
        this.inventoryInDate = inventoryInDate;
    }
}
