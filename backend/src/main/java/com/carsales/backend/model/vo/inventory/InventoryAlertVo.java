package com.carsales.backend.model.vo.inventory;

public class InventoryAlertVo {

    private Integer modelId;
    private Integer brandId;
    private String brandName;
    private String modelName;
    private Integer modelYear;
    private String trimName;
    private Integer safetyStockThreshold;
    private Integer inInventoryCount;
    private Integer lockedCount;
    private Integer inTransitCount;
    private Integer shortageCount;

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

    public Integer getSafetyStockThreshold() {
        return safetyStockThreshold;
    }

    public void setSafetyStockThreshold(Integer safetyStockThreshold) {
        this.safetyStockThreshold = safetyStockThreshold;
    }

    public Integer getInInventoryCount() {
        return inInventoryCount;
    }

    public void setInInventoryCount(Integer inInventoryCount) {
        this.inInventoryCount = inInventoryCount;
    }

    public Integer getLockedCount() {
        return lockedCount;
    }

    public void setLockedCount(Integer lockedCount) {
        this.lockedCount = lockedCount;
    }

    public Integer getInTransitCount() {
        return inTransitCount;
    }

    public void setInTransitCount(Integer inTransitCount) {
        this.inTransitCount = inTransitCount;
    }

    public Integer getShortageCount() {
        return shortageCount;
    }

    public void setShortageCount(Integer shortageCount) {
        this.shortageCount = shortageCount;
    }
}
