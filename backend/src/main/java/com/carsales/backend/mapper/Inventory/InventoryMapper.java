package com.carsales.backend.mapper.Inventory;

import com.carsales.backend.model.dto.Inventory.InventoryVehicleQueryDto;
import com.carsales.backend.model.vo.inventory.InventoryVehicleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InventoryMapper {

    List<InventoryVehicleVo> queryVehicles(@Param("query") InventoryVehicleQueryDto query,
                                           @Param("offset") int offset,
                                           @Param("limit") int limit);

    long countVehicles(@Param("query") InventoryVehicleQueryDto query);
}
