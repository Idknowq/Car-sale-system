package com.carsales.backend.controller.report;

import com.carsales.backend.common.api.ApiResponse;
import com.carsales.backend.model.dto.report.BestSellingModelQueryDto;
import com.carsales.backend.model.dto.report.MonthlySalesReportQueryDto;
import com.carsales.backend.model.dto.report.SalesPerformanceRankingQueryDto;
import com.carsales.backend.model.vo.common.PageResult;
import com.carsales.backend.model.vo.report.BestSellingModelRankingItemVo;
import com.carsales.backend.model.vo.report.MonthlySalesReportItemVo;
import com.carsales.backend.model.vo.report.SalesPerformanceRankingItemVo;
import com.carsales.backend.service.report.ReportService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/ping")
    public ApiResponse<String> ping() {
        return ApiResponse.ok("report-ok");
    }

    @GetMapping("/sales-performance/ranking")
    public ApiResponse<List<SalesPerformanceRankingItemVo>> querySalesPerformanceRanking(@Valid SalesPerformanceRankingQueryDto query) {
        return ApiResponse.ok(reportService.querySalesPerformanceRanking(query));
    }

    @GetMapping("/best-selling-models")
    public ApiResponse<List<BestSellingModelRankingItemVo>> queryBestSellingModels(@Valid BestSellingModelQueryDto query) {
        return ApiResponse.ok(reportService.queryBestSellingModels(query));
    }

    @GetMapping("/monthly-sales")
    public ApiResponse<PageResult<MonthlySalesReportItemVo>> queryMonthlySalesReport(@Valid MonthlySalesReportQueryDto query) {
        return ApiResponse.ok(reportService.queryMonthlySalesReport(query));
    }
}
