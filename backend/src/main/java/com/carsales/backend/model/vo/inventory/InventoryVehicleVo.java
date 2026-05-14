package com.carsales.backend.model.vo.inventory;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InventoryVehicleVo {

    private String vehicleVin;
    private Integer modelId;
    private Integer brandId;
    private String brandName;
    private String modelName;
    private Integer modelYear;
    private String trimName;
    private String engineNo;
    private String color;
    private LocalDate manufactureDate;
    private LocalDate inventoryInDate;
    private BigDecimal purchaseCost;
    private BigDecimal suggestedRetailPrice;
    private String currentStatus;

    public String getVehicleVin() {
        return vehicleVin;
    }

    public void setVehicleVin(String vehicleVin) {
        this.vehicleVin = vehicleVin;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Integer getModelYear() {
        return modelYear;
    }

    public void setModelYear(Integer modelYear) {
        this.modelYear = modelYear;
    }

    public String getTrimName() {
        return trimName;
    }

    public void setTrimName(String trimName) {
        this.trimName = trimName;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public LocalDate getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(LocalDate manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public LocalDate getInventoryInDate() {
        return inventoryInDate;
    }

    public void setInventoryInDate(LocalDate inventoryInDate) {
        this.inventoryInDate = inventoryInDate;
    }

    public BigDecimal getPurchaseCost() {
        return purchaseCost;
    }

    public void setPurchaseCost(BigDecimal purchaseCost) {
        this.purchaseCost = purchaseCost;
    }

    public BigDecimal getSuggestedRetailPrice() {
        return suggestedRetailPrice;
    }

    public void setSuggestedRetailPrice(BigDecimal suggestedRetailPrice) {
        this.suggestedRetailPrice = suggestedRetailPrice;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }
}
