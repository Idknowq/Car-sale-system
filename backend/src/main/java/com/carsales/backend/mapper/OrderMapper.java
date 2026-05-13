package com.carsales.backend.mapper;

import com.carsales.backend.model.vo.SalesOrderVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface OrderMapper {
    void callCreateSalesOrder(Map<String, Object> params);

    SalesOrderVo selectOrderById(@Param("orderId") Integer orderId);
}
