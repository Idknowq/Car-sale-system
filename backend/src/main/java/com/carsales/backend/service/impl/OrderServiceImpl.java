package com.carsales.backend.service.impl;

import com.carsales.backend.common.exception.BizException;
import com.carsales.backend.mapper.OrderMapper;
import com.carsales.backend.model.dto.CreateSalesOrderRequest;
import com.carsales.backend.model.dto.MyOrderQueryRequest;
import com.carsales.backend.model.vo.CreateSalesOrderResponse;
import com.carsales.backend.model.vo.MyOrderItemVo;
import com.carsales.backend.model.vo.PageResult;
import com.carsales.backend.model.vo.SalesOrderVo;
import com.carsales.backend.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    private static final int MAX_PAGE_SIZE = 100;

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

    @Override
    public PageResult<MyOrderItemVo> queryMyOrders(MyOrderQueryRequest request) {
        if (request.getStaffId() == null) {
            throw new BizException(40002, "staffId is required");
        }

        int pageNo = request.getPageNo() == null ? 1 : request.getPageNo();
        int pageSize = request.getPageSize() == null ? 10 : request.getPageSize();

        if (pageNo <= 0) {
            throw new BizException(40003, "pageNo must be greater than 0");
        }
        if (pageSize <= 0 || pageSize > MAX_PAGE_SIZE) {
            throw new BizException(40004, "pageSize must be between 1 and " + MAX_PAGE_SIZE);
        }

        if (request.getCreatedAtStart() != null && request.getCreatedAtEnd() != null
                && request.getCreatedAtStart().isAfter(request.getCreatedAtEnd())) {
            throw new BizException(40005, "createdAtStart cannot be after createdAtEnd");
        }

        int offset = (pageNo - 1) * pageSize;

        List<MyOrderItemVo> records = orderMapper.selectMyOrders(
                request.getStaffId(),
                request.getCustomerId(),
                request.getCustomerName(),
                request.getPhone(),
                request.getOrderStatus(),
                request.getVehicleVin(),
                request.getCreatedAtStart(),
                request.getCreatedAtEnd(),
                offset,
                pageSize
        );

        long total = orderMapper.countMyOrders(
                request.getStaffId(),
                request.getCustomerId(),
                request.getCustomerName(),
                request.getPhone(),
                request.getOrderStatus(),
                request.getVehicleVin(),
                request.getCreatedAtStart(),
                request.getCreatedAtEnd()
        );

        return new PageResult<>(total, pageNo, pageSize, records);
    }
}
