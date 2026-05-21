package com.carsales.backend.service.report.impl;

import com.carsales.backend.common.exception.BizException;
import com.carsales.backend.mapper.report.ReportMapper;
import com.carsales.backend.model.dto.report.BestSellingModelQueryDto;
import com.carsales.backend.model.dto.report.MonthlySalesReportQueryDto;
import com.carsales.backend.model.dto.report.SalesPerformanceRankingQueryDto;
import com.carsales.backend.model.vo.report.BestSellingModelRankingItemVo;
import com.carsales.backend.model.vo.report.MonthlySalesReportItemVo;
import com.carsales.backend.model.vo.report.SalesPerformanceRankingItemVo;
import com.carsales.backend.service.report.ReportService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportMapper reportMapper;

    public ReportServiceImpl(ReportMapper reportMapper) {
        this.reportMapper = reportMapper;
    }

    @Override
    public List<SalesPerformanceRankingItemVo> querySalesPerformanceRanking(SalesPerformanceRankingQueryDto query) {
        validatePeriodCondition(query);
        return reportMapper.selectSalesPerformanceRanking(
                query.getPeriodType(),
                query.getStatYear(),
                query.getStatQuarter(),
                query.getStatMonth(),
                query.getTopN()
        );
    }

    @Override
    public List<BestSellingModelRankingItemVo> queryBestSellingModels(BestSellingModelQueryDto query) {
        Integer topN = query.getTopN() == null ? 5 : query.getTopN();
        if (topN <= 0 || topN > 100) {
            throw new BizException(40021, "topN must be between 1 and 100");
        }
        return reportMapper.selectBestSellingModels(topN);
    }

    @Override
    public List<MonthlySalesReportItemVo> queryMonthlySalesReport(MonthlySalesReportQueryDto query) {
        Integer year = query.getYear();
        Integer month = query.getMonth();
        if (year == null || year < 2000 || year > 2100) {
            throw new BizException(40031, "year must be between 2000 and 2100");
        }
        if (month == null || month < 1 || month > 12) {
            throw new BizException(40032, "month must be between 1 and 12");
        }

        try {
            return reportMapper.selectMonthlySalesReport(year, month);
        } catch (Exception ex) {
            throw new BizException(50031, "Query monthly sales report failed: " + ex.getMessage());
        }
    }

    private void validatePeriodCondition(SalesPerformanceRankingQueryDto query) {
        if ("MONTH".equals(query.getPeriodType()) && query.getStatMonth() == null) {
            throw new BizException(40011, "statMonth is required when periodType is MONTH");
        }
        if ("QUARTER".equals(query.getPeriodType()) && query.getStatQuarter() == null) {
            throw new BizException(40012, "statQuarter is required when periodType is QUARTER");
        }
        if ("MONTH".equals(query.getPeriodType()) && query.getStatQuarter() != null) {
            throw new BizException(40013, "statQuarter must be null when periodType is MONTH");
        }
        if ("QUARTER".equals(query.getPeriodType()) && query.getStatMonth() != null) {
            throw new BizException(40014, "statMonth must be null when periodType is QUARTER");
        }
    }
}
