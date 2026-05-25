package com.carsales.backend.model.dto.report;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class MonthlySalesReportQueryDto {

    @NotNull(message = "year is required")
    @Min(value = 2000, message = "year must be >= 2000")
    @Max(value = 2100, message = "year must be <= 2100")
    private Integer year;

    @NotNull(message = "month is required")
    @Min(value = 1, message = "month must be >= 1")
    @Max(value = 12, message = "month must be <= 12")
    private Integer month;

    @Min(value = 1, message = "pageNo must be >= 1")
    private Integer pageNo = 1;

    @Min(value = 1, message = "pageSize must be >= 1")
    @Max(value = 100, message = "pageSize must be <= 100")
    private Integer pageSize = 10;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
