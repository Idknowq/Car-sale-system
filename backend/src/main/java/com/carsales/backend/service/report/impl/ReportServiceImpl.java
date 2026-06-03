package com.carsales.backend.service.report.impl;

import com.carsales.backend.common.exception.BizException;
import com.carsales.backend.mapper.report.ReportMapper;
import com.carsales.backend.model.dto.report.BestSellingModelQueryDto;
import com.carsales.backend.model.dto.report.MonthlySalesReportQueryDto;
import com.carsales.backend.model.dto.report.SalesPerformanceRankingQueryDto;
import com.carsales.backend.model.vo.common.PageResult;
import com.carsales.backend.model.vo.report.BestSellingModelRankingItemVo;
import com.carsales.backend.model.vo.report.MonthlySalesReportItemVo;
import com.carsales.backend.model.vo.report.SalesPerformanceRankingItemVo;
import com.carsales.backend.service.report.ReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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
    @Transactional(readOnly = true)
    public PageResult<MonthlySalesReportItemVo> queryMonthlySalesReport(MonthlySalesReportQueryDto query) {
        Integer year = query.getYear();
        Integer month = query.getMonth();
        if (year == null || year < 2000 || year > 2100) {
            throw new BizException(40031, "year must be between 2000 and 2100");
        }
        if (month == null || month < 1 || month > 12) {
            throw new BizException(40032, "month must be between 1 and 12");
        }
        int pageNo = query.getPageNo() == null ? 1 : query.getPageNo();
        int pageSize = query.getPageSize() == null ? 10 : query.getPageSize();
        if (pageNo <= 0) {
            throw new BizException(40033, "pageNo must be >= 1");
        }
        if (pageSize <= 0 || pageSize > 100) {
            throw new BizException(40034, "pageSize must be between 1 and 100");
        }

        try {
            int offset = (pageNo - 1) * pageSize;
            String cursorName = buildMonthlyReportCursorName();
            reportMapper.callMonthlySalesReportProcedure(year, month, cursorName);
            List<MonthlySalesReportItemVo> allRecords = reportMapper.fetchMonthlySalesReport(cursorName);
            int total = allRecords.size();
            int fromIndex = Math.min(offset, total);
            int toIndex = Math.min(offset + pageSize, total);
            return new PageResult<>(total, pageNo, pageSize, allRecords.subList(fromIndex, toIndex));
        } catch (Exception ex) {
            throw new BizException(50031, "Query monthly sales report failed: " + ex.getMessage());
        }
    }

    private String buildMonthlyReportCursorName() {
        return "cur_monthly_report_" + UUID.randomUUID().toString().replace("-", "");
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
