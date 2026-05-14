package com.carsales.backend.service.sales;

import com.carsales.backend.model.dto.sales.CreateSalesOrderRequest;
import com.carsales.backend.model.dto.sales.MyOrderQueryRequest;
import com.carsales.backend.model.vo.sales.CreateSalesOrderResponse;
import com.carsales.backend.model.vo.sales.MyOrderItemVo;
import com.carsales.backend.model.vo.common.PageResult;
import com.carsales.backend.model.vo.sales.SalesOrderVo;

public interface OrderService {
    CreateSalesOrderResponse createSalesOrder(CreateSalesOrderRequest request);

    SalesOrderVo getOrderById(Integer orderId);

    PageResult<MyOrderItemVo> queryMyOrders(MyOrderQueryRequest request);
}
