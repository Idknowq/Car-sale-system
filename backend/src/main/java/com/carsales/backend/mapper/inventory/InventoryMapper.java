package com.carsales.backend.mapper.inventory;

import com.carsales.backend.model.dto.inventory.InventoryAlertQueryDto;
import com.carsales.backend.model.dto.inventory.InventoryVehicleQueryDto;
import com.carsales.backend.model.vo.inventory.InventoryAlertVo;
import com.carsales.backend.model.vo.inventory.InventoryVehicleInboundCheckVo;
import com.carsales.backend.model.vo.inventory.InventoryVehicleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface InventoryMapper {

    List<InventoryVehicleVo> queryVehicles(@Param("query") InventoryVehicleQueryDto query,
                                           @Param("offset") int offset,
                                           @Param("limit") int limit);

    long countVehicles(@Param("query") InventoryVehicleQueryDto query);

    InventoryVehicleInboundCheckVo selectInboundCheckByVin(@Param("vehicleVin") String vehicleVin);

    int updateVehicleInbound(@Param("vehicleVin") String vehicleVin,
                             @Param("inventoryInDate") LocalDate inventoryInDate);

    int insertVehicleStatusLog(@Param("vehicleVin") String vehicleVin,
                               @Param("staffId") Integer staffId,
                               @Param("reason") String reason);

    int countStaffById(@Param("staffId") Integer staffId);

    List<InventoryAlertVo> queryInventoryAlerts(@Param("query") InventoryAlertQueryDto query);
}
