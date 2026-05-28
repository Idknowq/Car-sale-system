package com.carsales.backend.service.sales.impl;

import com.carsales.backend.common.exception.BizException;
import com.carsales.backend.mapper.sales.CustomerIntentMapper;
import com.carsales.backend.mapper.sales.OrderMapper;
import com.carsales.backend.model.dto.sales.CreateCustomerIntentRequest;
import com.carsales.backend.model.dto.sales.CreateSalesOrderRequest;
import com.carsales.backend.model.dto.sales.CompleteSalesOrderRequest;
import com.carsales.backend.model.dto.sales.MyOrderQueryRequest;
import com.carsales.backend.model.vo.sales.CreateCustomerIntentResponse;
import com.carsales.backend.model.vo.sales.CreateSalesOrderResponse;
import com.carsales.backend.model.vo.sales.CompleteSalesOrderResponse;
import com.carsales.backend.model.vo.sales.MyOrderItemVo;
import com.carsales.backend.model.vo.common.PageResult;
import com.carsales.backend.model.vo.sales.SalesOrderVo;
import com.carsales.backend.service.sales.OrderService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {

    private static final int MAX_PAGE_SIZE = 100;
    private static final Set<String> ALLOWED_GENDERS = Set.of("M", "F", "OTHER");
    private static final Set<String> ALLOWED_INTENT_LEVELS = Set.of("HIGH", "MEDIUM", "LOW");

    private final OrderMapper orderMapper;
    private final CustomerIntentMapper customerIntentMapper;

    public OrderServiceImpl(OrderMapper orderMapper, CustomerIntentMapper customerIntentMapper) {
        this.orderMapper = orderMapper;
        this.customerIntentMapper = customerIntentMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CreateSalesOrderResponse createSalesOrder(CreateSalesOrderRequest request) {
        // The database procedure and triggers own order creation and inventory locking.
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
        } catch (DuplicateKeyException ex) {
            throw new BizException(40910, "Vehicle already has a sales order: " + request.getVehicleVin());
        } catch (Exception ex) {
            if (isVehicleOrderDuplicate(ex)) {
                throw new BizException(40910, "Vehicle already has a sales order: " + request.getVehicleVin());
            }
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CreateCustomerIntentResponse createCustomerIntent(CreateCustomerIntentRequest request) {
        validateIntentRequest(request);
        validateModelAndStaff(request.getModelId(), request.getStaffId());

        // Reuse customers by phone, but verify identity fields before attaching a new intent.
        Integer customerId = customerIntentMapper.selectCustomerIdByPhone(request.getPhone());
        if (customerId != null) {
            validateExistingCustomerConsistency(customerId, request);
        } else {
            Integer insertedCustomerId = null;
            try {
                insertedCustomerId = customerIntentMapper.insertCustomerIfAbsent(
                        request.getCustomerName(),
                        request.getGender(),
                        request.getPhone(),
                        request.getIdCard(),
                        request.getAddress(),
                        request.getFirstVisitDate() == null ? LocalDate.now() : request.getFirstVisitDate(),
                        request.getSourceChannel()
                );
            } catch (DuplicateKeyException ex) {
                Integer existingByPhone = customerIntentMapper.selectCustomerIdByPhone(request.getPhone());
                if (existingByPhone != null) {
                    customerId = existingByPhone;
                } else if (customerIntentMapper.selectCustomerIdByIdCard(request.getIdCard()) != null) {
                    throw new BizException(40902, "idCard already exists");
                } else {
                    throw new BizException(50005, "Create customer intent failed: duplicate customer conflict");
                }
            }
            if (customerId == null) {
                customerId = insertedCustomerId == null
                        ? customerIntentMapper.selectCustomerIdByPhone(request.getPhone())
                        : insertedCustomerId;
            }
            if (customerId != null) {
                validateExistingCustomerConsistency(customerId, request);
            }
        }

        if (customerId == null) {
            throw new BizException(50002, "Create customer intent failed: customer id not resolved");
        }

        Integer intentId = customerIntentMapper.insertCustomerIntent(
                customerId,
                request.getModelId(),
                request.getIntentLevel(),
                request.getRemark(),
                request.getStaffId(),
                request.getNextContactTime()
        );

        if (intentId == null) {
            throw new BizException(50003, "Create customer intent failed: intent id not returned");
        }

        return new CreateCustomerIntentResponse(customerId, intentId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CompleteSalesOrderResponse completeSalesOrder(Integer orderId, CompleteSalesOrderRequest request) {
        SalesOrderVo order = orderMapper.selectOrderById(orderId);
        if (order == null) {
            throw new BizException(40401, "Order not found: " + orderId);
        }
        if (!Objects.equals(order.getStaffId(), request.getStaffId())) {
            throw new BizException(40921, "staffId does not match order owner");
        }

        String status = order.getOrderStatus();
        if ("COMPLETED".equals(status)) {
            return new CompleteSalesOrderResponse(orderId, "COMPLETED");
        }
        if ("CANCELLED".equals(status)) {
            throw new BizException(40922, "Cancelled order cannot be completed: " + orderId);
        }
        if (!"LOCKED".equals(status) && !"DEPOSIT_PAID".equals(status)) {
            throw new BizException(40923, "Current order status cannot be completed: " + status);
        }

        try {
            // The update trigger moves the vehicle from LOCKED to SOLD in the same transaction.
            int affected = orderMapper.updateOrderStatusToCompleted(orderId);
            if (affected <= 0) {
                throw new BizException(50006, "Complete order failed: no row updated");
            }
        } catch (Exception ex) {
            if (isDeliveryTriggerConflict(ex)) {
                throw new BizException(40924, "Complete order failed: vehicle status is not LOCKED");
            }
            throw new BizException(40006, "Complete order failed: " + ex.getMessage());
        }

        return new CompleteSalesOrderResponse(orderId, "COMPLETED");
    }

    private void validateIntentRequest(CreateCustomerIntentRequest request) {
        if (request.getGender() != null && !ALLOWED_GENDERS.contains(request.getGender())) {
            throw new BizException(40006, "gender must be one of M/F/OTHER");
        }
        if (!ALLOWED_INTENT_LEVELS.contains(request.getIntentLevel())) {
            throw new BizException(40007, "intentLevel must be one of HIGH/MEDIUM/LOW");
        }
        LocalDateTime nextContactTime = request.getNextContactTime();
        if (nextContactTime != null && nextContactTime.isBefore(LocalDateTime.now())) {
            throw new BizException(40008, "nextContactTime cannot be earlier than now");
        }

        Integer customerIdByIdCard = customerIntentMapper.selectCustomerIdByIdCard(request.getIdCard());
        Integer customerIdByPhone = customerIntentMapper.selectCustomerIdByPhone(request.getPhone());
        if (customerIdByIdCard != null && customerIdByPhone != null && !customerIdByIdCard.equals(customerIdByPhone)) {
            throw new BizException(40901, "phone and idCard belong to different customers");
        }
    }

    private void validateExistingCustomerConsistency(Integer customerId, CreateCustomerIntentRequest request) {
        CustomerIntentMapper.ExistingCustomerSnapshot snapshot = customerIntentMapper.selectCustomerSnapshotById(customerId);
        if (snapshot == null) {
            throw new BizException(50004, "customer not found while checking consistency: " + customerId);
        }
        if (!Objects.equals(normalizeText(snapshot.getIdCard()), normalizeText(request.getIdCard()))) {
            throw new BizException(40901, "phone and idCard belong to different customers");
        }
        if (!Objects.equals(normalizeText(snapshot.getCustomerName()), normalizeText(request.getCustomerName()))
                || !Objects.equals(normalizeText(snapshot.getGender()), normalizeText(request.getGender()))
                || !Objects.equals(normalizeText(snapshot.getAddress()), normalizeText(request.getAddress()))
                || !Objects.equals(normalizeText(snapshot.getSourceChannel()), normalizeText(request.getSourceChannel()))) {
            throw new BizException(40903, "customer profile mismatch for existing phone");
        }
    }

    private void validateModelAndStaff(Integer modelId, Integer staffId) {
        if (customerIntentMapper.selectModelId(modelId) == null) {
            throw new BizException(40402, "model not found: " + modelId);
        }
        if (customerIntentMapper.selectStaffId(staffId) == null) {
            throw new BizException(40403, "staff not found: " + staffId);
        }
    }

    private boolean isVehicleOrderDuplicate(Exception ex) {
        String message = ex.getMessage();
        if (message == null) {
            return false;
        }
        return message.contains("uk_sales_order_vehicle_vin")
                || message.contains("duplicate key value violates unique constraint");
    }

    private boolean isDeliveryTriggerConflict(Exception ex) {
        String message = ex.getMessage();
        if (message == null) {
            return false;
        }
        return message.contains("cannot mark SOLD for order")
                || message.contains("is not in LOCKED status");
    }

    private String normalizeText(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim();
        return normalized.isEmpty() ? null : normalized;
    }
}
