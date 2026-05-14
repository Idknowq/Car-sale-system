package com.carsales.backend.mapper;

import com.carsales.backend.model.vo.MyOrderItemVo;
import com.carsales.backend.model.vo.SalesOrderVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    void callCreateSalesOrder(Map<String, Object> params);

    SalesOrderVo selectOrderById(@Param("orderId") Integer orderId);

    List<MyOrderItemVo> selectMyOrders(
            @Param("staffId") Integer staffId,
            @Param("customerId") Integer customerId,
            @Param("customerName") String customerName,
            @Param("phone") String phone,
            @Param("orderStatus") String orderStatus,
            @Param("vehicleVin") String vehicleVin,
            @Param("createdAtStart") LocalDateTime createdAtStart,
            @Param("createdAtEnd") LocalDateTime createdAtEnd,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize
    );

    long countMyOrders(
            @Param("staffId") Integer staffId,
            @Param("customerId") Integer customerId,
            @Param("customerName") String customerName,
            @Param("phone") String phone,
            @Param("orderStatus") String orderStatus,
            @Param("vehicleVin") String vehicleVin,
            @Param("createdAtStart") LocalDateTime createdAtStart,
            @Param("createdAtEnd") LocalDateTime createdAtEnd
    );
}
