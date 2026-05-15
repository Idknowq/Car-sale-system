package com.carsales.backend.service.inventory;

import com.carsales.backend.model.dto.inventory.InventoryAlertQueryDto;
import com.carsales.backend.model.dto.inventory.InventoryVehicleQueryDto;
import com.carsales.backend.model.dto.inventory.VehicleInboundRequest;
import com.carsales.backend.model.vo.common.PageResult;
import com.carsales.backend.model.vo.inventory.InventoryAlertVo;
import com.carsales.backend.model.vo.inventory.InventoryVehicleVo;
import com.carsales.backend.model.vo.inventory.VehicleInboundResponse;

import java.util.List;

public interface InventoryService {

    PageResult<InventoryVehicleVo> queryVehicles(InventoryVehicleQueryDto query);

    VehicleInboundResponse inboundVehicle(VehicleInboundRequest request);

    List<InventoryAlertVo> queryInventoryAlerts(InventoryAlertQueryDto query);
}
