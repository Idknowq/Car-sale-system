package com.carsales.backend.mapper.report;

import com.carsales.backend.model.vo.report.BestSellingModelRankingItemVo;
import com.carsales.backend.model.vo.report.MonthlySalesReportItemVo;
import com.carsales.backend.model.vo.report.SalesPerformanceRankingItemVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReportMapper {
    List<SalesPerformanceRankingItemVo> selectSalesPerformanceRanking(
            @Param("periodType") String periodType,
            @Param("statYear") Integer statYear,
            @Param("statQuarter") Integer statQuarter,
            @Param("statMonth") Integer statMonth,
            @Param("topN") Integer topN
    );

    List<BestSellingModelRankingItemVo> selectBestSellingModels(@Param("topN") Integer topN);

    List<MonthlySalesReportItemVo> selectMonthlySalesReportPage(
            @Param("year") Integer year,
            @Param("month") Integer month,
            @Param("offset") Integer offset,
            @Param("pageSize") Integer pageSize
    );

    long countMonthlySalesReport(
            @Param("year") Integer year,
            @Param("month") Integer month
    );
}
