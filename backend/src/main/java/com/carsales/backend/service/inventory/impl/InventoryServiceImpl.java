package com.carsales.backend.service.inventory.impl;

import com.carsales.backend.common.exception.BizException;
import com.carsales.backend.mapper.inventory.InventoryMapper;
import com.carsales.backend.model.dto.inventory.InventoryAlertQueryDto;
import com.carsales.backend.model.dto.inventory.InventoryVehicleQueryDto;
import com.carsales.backend.model.dto.inventory.VehicleInboundRequest;
import com.carsales.backend.model.vo.common.PageResult;
import com.carsales.backend.model.vo.inventory.InventoryAlertVo;
import com.carsales.backend.model.vo.inventory.InventoryVehicleInboundCheckVo;
import com.carsales.backend.model.vo.inventory.InventoryVehicleVo;
import com.carsales.backend.model.vo.inventory.VehicleInboundResponse;
import com.carsales.backend.service.inventory.InventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    private static final String STATUS_IN_TRANSIT = "IN_TRANSIT";
    private static final String STATUS_IN_INVENTORY = "IN_INVENTORY";

    private final InventoryMapper inventoryMapper;

    public InventoryServiceImpl(InventoryMapper inventoryMapper) {
        this.inventoryMapper = inventoryMapper;
    }

    @Override
    public PageResult<InventoryVehicleVo> queryVehicles(InventoryVehicleQueryDto query) {
        validateDateRange(query.getInventoryInDateStart(), query.getInventoryInDateEnd(), "inventoryInDate");
        validateDateRange(query.getManufactureDateStart(), query.getManufactureDateEnd(), "manufactureDate");

        int pageNo = query.getPageNum() == null ? 1 : query.getPageNum();
        int pageSize = query.getPageSize() == null ? 10 : query.getPageSize();
        int offset = (pageNo - 1) * pageSize;

        List<InventoryVehicleVo> records = inventoryMapper.queryVehicles(query, offset, pageSize);
        long total = inventoryMapper.countVehicles(query);
        return new PageResult<>(total, pageNo, pageSize, records);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VehicleInboundResponse inboundVehicle(VehicleInboundRequest request) {
        String vehicleVin = request.getVehicleVin().trim();
        LocalDate inboundDate = request.getInventoryInDate() == null ? LocalDate.now() : request.getInventoryInDate();
        String reason = normalizeReason(request.getReason());

        InventoryVehicleInboundCheckVo vehicle = inventoryMapper.selectInboundCheckByVin(vehicleVin);
        if (vehicle == null) {
            throw new BizException(40411, "Vehicle not found: " + vehicleVin);
        }

        if (!STATUS_IN_TRANSIT.equals(vehicle.getCurrentStatus())) {
            throw new BizException(40011, "Vehicle status is " + vehicle.getCurrentStatus() + ", expected IN_TRANSIT");
        }

        LocalDate manufactureDate = vehicle.getManufactureDate();
        if (manufactureDate != null && inboundDate.isBefore(manufactureDate)) {
            throw new BizException(40012, "inventoryInDate cannot be earlier than manufactureDate");
        }

        int updated = inventoryMapper.updateVehicleInbound(vehicleVin, inboundDate);
        if (updated == 0) {
            throw new BizException(40911, "Vehicle inbound failed due to status change, please retry");
        }

        inventoryMapper.insertVehicleStatusLog(vehicleVin, request.getStaffId(), reason);

        return new VehicleInboundResponse(vehicleVin, STATUS_IN_TRANSIT, STATUS_IN_INVENTORY, inboundDate);
    }

    @Override
    public List<InventoryAlertVo> queryInventoryAlerts(InventoryAlertQueryDto query) {
        return inventoryMapper.queryInventoryAlerts(query);
    }

    private void validateDateRange(LocalDate start, LocalDate end, String field) {
        if (start != null && end != null && start.isAfter(end)) {
            throw new BizException(400, field + " start must be <= end");
        }
    }

    private String normalizeReason(String reason) {
        if (reason == null || reason.isBlank()) {
            return "Vehicle inbound";
        }
        return reason.trim();
    }
}
