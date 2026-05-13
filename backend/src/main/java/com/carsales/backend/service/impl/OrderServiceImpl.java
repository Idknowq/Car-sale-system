package com.carsales.backend.service.impl;

import com.carsales.backend.common.exception.BizException;
import com.carsales.backend.mapper.OrderMapper;
import com.carsales.backend.model.dto.CreateSalesOrderRequest;
import com.carsales.backend.model.vo.CreateSalesOrderResponse;
import com.carsales.backend.model.vo.SalesOrderVo;
import com.carsales.backend.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CreateSalesOrderResponse createSalesOrder(CreateSalesOrderRequest request) {
        Map<String, Object> params = new HashMap<>();
        params.put("customerId", request.getCustomerId());
        params.put("staffId", request.getStaffId());
        params.put("vehicleVin", request.getVehicleVin());
        params.put("depositAmount", request.getDepositAmount());
        params.put("vehicleAmount", request.getVehicleAmount());
        params.put("optionAmount", request.getOptionAmount());
        params.put("insuranceAmount", request.getInsuranceAmount());
        params.put("discountAmount", request.getDiscountAmount());
        params.put("otherAmount", request.getOtherAmount());
        try {
            orderMapper.callCreateSalesOrder(params);
        } catch (Exception ex) {
            throw new BizException(40001, "Create sales order failed: " + ex.getMessage());
        }

        Object orderIdObj = params.get("orderId");
        if (!(orderIdObj instanceof Integer orderId)) {
            throw new BizException(50001, "Create sales order failed: order id not returned");
        }
        return new CreateSalesOrderResponse(orderId);
    }

    @Override
    public SalesOrderVo getOrderById(Integer orderId) {
        SalesOrderVo order = orderMapper.selectOrderById(orderId);
        if (order == null) {
            throw new BizException(40401, "Order not found: " + orderId);
        }
        return order;
    }
}
