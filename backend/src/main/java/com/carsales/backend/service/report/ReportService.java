package com.carsales.backend.service.report;

import com.carsales.backend.model.dto.report.SalesPerformanceRankingQueryDto;
import com.carsales.backend.model.vo.report.SalesPerformanceRankingItemVo;

import java.util.List;

public interface ReportService {
    List<SalesPerformanceRankingItemVo> querySalesPerformanceRanking(SalesPerformanceRankingQueryDto query);
}
