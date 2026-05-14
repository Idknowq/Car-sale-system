package com.carsales.backend.service.inventory;

import com.carsales.backend.model.dto.Inventory.InventoryVehicleQueryDto;
import com.carsales.backend.model.vo.common.PageResult;
import com.carsales.backend.model.vo.inventory.InventoryVehicleVo;

public interface InventoryService {

    PageResult<InventoryVehicleVo> queryVehicles(InventoryVehicleQueryDto query);
}
