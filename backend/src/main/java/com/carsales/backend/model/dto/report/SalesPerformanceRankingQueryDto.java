package com.carsales.backend.model.dto.report;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class SalesPerformanceRankingQueryDto {

    @NotNull(message = "periodType is required")
    @Pattern(regexp = "^(MONTH|QUARTER)$", message = "periodType must be MONTH or QUARTER")
    private String periodType;

    @NotNull(message = "statYear is required")
    @Min(value = 2000, message = "statYear must be >= 2000")
    @Max(value = 2100, message = "statYear must be <= 2100")
    private Integer statYear;

    @Min(value = 1, message = "statQuarter must be >= 1")
    @Max(value = 4, message = "statQuarter must be <= 4")
    private Integer statQuarter;

    @Min(value = 1, message = "statMonth must be >= 1")
    @Max(value = 12, message = "statMonth must be <= 12")
    private Integer statMonth;

    @Min(value = 1, message = "topN must be >= 1")
    @Max(value = 100, message = "topN must be <= 100")
    private Integer topN = 10;

    public String getPeriodType() {
        return periodType;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }

    public Integer getStatYear() {
        return statYear;
    }

    public void setStatYear(Integer statYear) {
        this.statYear = statYear;
    }

    public Integer getStatQuarter() {
        return statQuarter;
    }

    public void setStatQuarter(Integer statQuarter) {
        this.statQuarter = statQuarter;
    }

    public Integer getStatMonth() {
        return statMonth;
    }

    public void setStatMonth(Integer statMonth) {
        this.statMonth = statMonth;
    }

    public Integer getTopN() {
        return topN;
    }

    public void setTopN(Integer topN) {
        this.topN = topN;
    }
}
