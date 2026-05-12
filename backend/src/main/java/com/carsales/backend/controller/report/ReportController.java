package com.carsales.backend.controller.report;

import com.carsales.backend.common.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
public class ReportController {
    @GetMapping("/ping")
    public ApiResponse<String> ping() { return ApiResponse.ok("report-ok"); }
}
