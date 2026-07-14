package DATN.example.demo.controller;


import DATN.example.demo.dto.response.DashboardSummaryResponse;
import DATN.example.demo.service.DashboardService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class DashboardController {
    DashboardService dashboardService;

    @GetMapping("/summary")
    public ApiResponse<DashboardSummaryResponse> getSummary(){
        return ApiResponse.<DashboardSummaryResponse>builder()
                .code(200)
                .message("Lấy dashboard thành công")
                .data(dashboardService.getSummary())
                .build();
    }
}
