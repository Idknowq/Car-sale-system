package com.carsales.backend.model.vo.report;

import java.math.BigDecimal;

public class SalesPerformanceRankingItemVo {
    private String periodType;
    private Integer statYear;
    private Integer statQuarter;
    private Integer statMonth;
    private String periodLabel;
    private Integer staffId;
    private String staffNo;
    private String staffName;
    private Integer orderCount;
    private BigDecimal salesAmount;
    private BigDecimal grossProfit;
    private Integer salesRank;

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

    public String getPeriodLabel() {
        return periodLabel;
    }

    public void setPeriodLabel(String periodLabel) {
        this.periodLabel = periodLabel;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public String getStaffNo() {
        return staffNo;
    }

    public void setStaffNo(String staffNo) {
        this.staffNo = staffNo;
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

    public Integer getSalesRank() {
        return salesRank;
    }

    public void setSalesRank(Integer salesRank) {
        this.salesRank = salesRank;
    }
}
