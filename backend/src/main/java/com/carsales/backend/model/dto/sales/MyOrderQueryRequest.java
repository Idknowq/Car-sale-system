package com.carsales.backend.model.dto.sales;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class MyOrderQueryRequest {
    private Integer staffId;
    private Integer customerId;
    private String customerName;
    private String phone;
    private String orderStatus;
    private String vehicleVin;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAtStart;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAtEnd;

    private Integer pageNo = 1;
    private Integer pageSize = 10;

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getVehicleVin() {
        return vehicleVin;
    }

    public void setVehicleVin(String vehicleVin) {
        this.vehicleVin = vehicleVin;
    }

    public LocalDateTime getCreatedAtStart() {
        return createdAtStart;
    }

    public void setCreatedAtStart(LocalDateTime createdAtStart) {
        this.createdAtStart = createdAtStart;
    }

    public LocalDateTime getCreatedAtEnd() {
        return createdAtEnd;
    }

    public void setCreatedAtEnd(LocalDateTime createdAtEnd) {
        this.createdAtEnd = createdAtEnd;
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
