package com.carsales.backend.model.dto.inventory;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class InventoryVehicleQueryDto {

    private String vehicleVin;

    private Integer brandId;

    private Integer modelId;

    @Pattern(regexp = "^(IN_TRANSIT|IN_INVENTORY|LOCKED|SOLD)?$", message = "currentStatus is invalid")
    private String currentStatus;

    private String color;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate inventoryInDateStart;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate inventoryInDateEnd;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate manufactureDateStart;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate manufactureDateEnd;

    @Min(value = 1, message = "pageNum must be >= 1")
    private Integer pageNum = 1;

    @Min(value = 1, message = "pageSize must be >= 1")
    @Max(value = 100, message = "pageSize must be <= 100")
    private Integer pageSize = 10;

    @Pattern(regexp = "^(inventoryInDate|manufactureDate|suggestedRetailPrice|purchaseCost|vehicleVin)?$", message = "sortBy is invalid")
    private String sortBy;

    @Pattern(regexp = "^(asc|desc|ASC|DESC)?$", message = "sortOrder is invalid")
    private String sortOrder;

    public String getVehicleVin() {
        return vehicleVin;
    }

    public void setVehicleVin(String vehicleVin) {
        this.vehicleVin = vehicleVin;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public LocalDate getInventoryInDateStart() {
        return inventoryInDateStart;
    }

    public void setInventoryInDateStart(LocalDate inventoryInDateStart) {
        this.inventoryInDateStart = inventoryInDateStart;
    }

    public LocalDate getInventoryInDateEnd() {
        return inventoryInDateEnd;
    }

    public void setInventoryInDateEnd(LocalDate inventoryInDateEnd) {
        this.inventoryInDateEnd = inventoryInDateEnd;
    }

    public LocalDate getManufactureDateStart() {
        return manufactureDateStart;
    }

    public void setManufactureDateStart(LocalDate manufactureDateStart) {
        this.manufactureDateStart = manufactureDateStart;
    }

    public LocalDate getManufactureDateEnd() {
        return manufactureDateEnd;
    }

    public void setManufactureDateEnd(LocalDate manufactureDateEnd) {
        this.manufactureDateEnd = manufactureDateEnd;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
