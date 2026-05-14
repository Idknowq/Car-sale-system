package com.carsales.backend.service;

import com.carsales.backend.model.dto.CreateSalesOrderRequest;
import com.carsales.backend.model.dto.MyOrderQueryRequest;
import com.carsales.backend.model.vo.CreateSalesOrderResponse;
import com.carsales.backend.model.vo.MyOrderItemVo;
import com.carsales.backend.model.vo.PageResult;
import com.carsales.backend.model.vo.SalesOrderVo;

public interface OrderService {
    CreateSalesOrderResponse createSalesOrder(CreateSalesOrderRequest request);

    SalesOrderVo getOrderById(Integer orderId);

    PageResult<MyOrderItemVo> queryMyOrders(MyOrderQueryRequest request);
}
