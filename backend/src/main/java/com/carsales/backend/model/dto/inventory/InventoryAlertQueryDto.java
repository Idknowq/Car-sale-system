package com.carsales.backend.model.dto.inventory;

public class InventoryAlertQueryDto {

    private Integer brandId;
    private Integer modelId;
    private Boolean onlyPositiveShortage = true;

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

    public Boolean getOnlyPositiveShortage() {
        return onlyPositiveShortage;
    }

    public void setOnlyPositiveShortage(Boolean onlyPositiveShortage) {
        this.onlyPositiveShortage = onlyPositiveShortage;
    }
}
