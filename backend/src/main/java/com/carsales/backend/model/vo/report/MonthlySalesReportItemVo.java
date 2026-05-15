package com.carsales.backend.model.vo.report;

import java.math.BigDecimal;

public class MonthlySalesReportItemVo {
    private Integer statYear;
    private Integer statMonth;
    private Integer staffId;
    private String staffName;
    private Integer orderCount;
    private BigDecimal salesAmount;
    private BigDecimal grossProfit;

    public Integer getStatYear() {
        return statYear;
    }

    public void setStatYear(Integer statYear) {
        this.statYear = statYear;
    }

    public Integer getStatMonth() {
        return statMonth;
    }

    public void setStatMonth(Integer statMonth) {
        this.statMonth = statMonth;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public BigDecimal getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(BigDecimal salesAmount) {
        this.salesAmount = salesAmount;
    }

    public BigDecimal getGrossProfit() {
        return grossProfit;
    }

    public void setGrossProfit(BigDecimal grossProfit) {
        this.grossProfit = grossProfit;
    }
}
