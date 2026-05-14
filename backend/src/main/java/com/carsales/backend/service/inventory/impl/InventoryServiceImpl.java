package com.carsales.backend.service.inventory.impl;

import com.carsales.backend.common.exception.BizException;
import com.carsales.backend.mapper.Inventory.InventoryMapper;
import com.carsales.backend.model.dto.Inventory.InventoryVehicleQueryDto;
import com.carsales.backend.model.vo.common.PageResult;
import com.carsales.backend.model.vo.inventory.InventoryVehicleVo;
import com.carsales.backend.service.inventory.InventoryService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

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

    private void validateDateRange(LocalDate start, LocalDate end, String field) {
        if (start != null && end != null && start.isAfter(end)) {
            throw new BizException(400, field + " start must be <= end");
        }
    }
}
