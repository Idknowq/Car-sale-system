package com.carsales.backend.service.report;

import com.carsales.backend.model.dto.report.BestSellingModelQueryDto;
import com.carsales.backend.model.dto.report.MonthlySalesReportQueryDto;
import com.carsales.backend.model.dto.report.SalesPerformanceRankingQueryDto;
import com.carsales.backend.model.vo.common.PageResult;
import com.carsales.backend.model.vo.report.BestSellingModelRankingItemVo;
import com.carsales.backend.model.vo.report.MonthlySalesReportItemVo;
import com.carsales.backend.model.vo.report.SalesPerformanceRankingItemVo;

import java.util.List;

public interface ReportService {
    List<SalesPerformanceRankingItemVo> querySalesPerformanceRanking(SalesPerformanceRankingQueryDto query);

    List<BestSellingModelRankingItemVo> queryBestSellingModels(BestSellingModelQueryDto query);

    PageResult<MonthlySalesReportItemVo> queryMonthlySalesReport(MonthlySalesReportQueryDto query);
}
