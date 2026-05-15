package com.carsales.backend.model.dto.report;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class BestSellingModelQueryDto {

    @Min(value = 1, message = "topN must be >= 1")
    @Max(value = 100, message = "topN must be <= 100")
    private Integer topN = 5;

    public Integer getTopN() {
        return topN;
    }

    public void setTopN(Integer topN) {
        this.topN = topN;
    }
}
