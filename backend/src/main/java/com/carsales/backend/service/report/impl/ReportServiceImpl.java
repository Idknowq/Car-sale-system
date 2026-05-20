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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportMapper reportMapper;
    private final JdbcTemplate jdbcTemplate;

    public ReportServiceImpl(ReportMapper reportMapper, JdbcTemplate jdbcTemplate) {
        this.reportMapper = reportMapper;
        this.jdbcTemplate = jdbcTemplate;
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
    public List<MonthlySalesReportItemVo> queryMonthlySalesReport(MonthlySalesReportQueryDto query) {
        Integer year = query.getYear();
        Integer month = query.getMonth();
        if (year == null || year < 2000 || year > 2100) {
            throw new BizException(40031, "year must be between 2000 and 2100");
        }
        if (month == null || month < 1 || month > 12) {
            throw new BizException(40032, "month must be between 1 and 12");
        }

        String cursorName = "cur_monthly_report";

        try {
            return jdbcTemplate.execute((org.springframework.jdbc.core.ConnectionCallback<java.util.List<com.carsales.backend.model.vo.report.MonthlySalesReportItemVo>>) connection -> {
                List<MonthlySalesReportItemVo> result = new ArrayList<>();
                String callSql = "{call sp_get_monthly_report(?, ?, ?)}";
                try (CallableStatement callableStatement = connection.prepareCall(callSql)) {
                    callableStatement.setInt(1, year);
                    callableStatement.setInt(2, month);
                    callableStatement.setString(3, cursorName);

                    callableStatement.execute();

                    String fetchSql = "FETCH ALL FROM " + cursorName;
                    try (Statement statement = connection.createStatement();
                         ResultSet rs = statement.executeQuery(fetchSql)) {
                        while (rs.next()) {
                            result.add(mapMonthlySalesRow(rs));
                        }
                    }
                }
                return result;
            });
        } catch (Exception ex) {
            throw new BizException(50031, "Query monthly sales report failed: " + ex.getMessage());
        }
    }

    private MonthlySalesReportItemVo mapMonthlySalesRow(ResultSet rs) throws java.sql.SQLException {
        MonthlySalesReportItemVo item = new MonthlySalesReportItemVo();
        item.setStatYear(rs.getInt("stat_year"));
        item.setStatMonth(rs.getInt("stat_month"));
        item.setStaffId(rs.getInt("staff_id"));
        item.setStaffName(rs.getString("staff_name"));
        item.setOrderCount(rs.getInt("order_count"));
        item.setSalesAmount(rs.getBigDecimal("sales_amount"));
        item.setGrossProfit(rs.getBigDecimal("gross_profit"));
        return item;
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
