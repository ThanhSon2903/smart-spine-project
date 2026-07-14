package DATN.example.demo.controller;


import DATN.example.demo.dto.request.AlertRequest;
import DATN.example.demo.dto.response.AlertResponse;
import DATN.example.demo.service.AlertService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alert")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AlertController {

    AlertService alertService;
    @PostMapping("/create")
    public ApiResponse<String> createAlert(@RequestBody AlertRequest alertRequest){
        return ApiResponse.<String>builder()
                .code(200)
                .message("Tạo cảnh báo thành công")
                .data(alertService.createAlert(alertRequest))
                .build();
    }

    @GetMapping("/get/{sessionId}")
    public ApiResponse<List<AlertResponse>> getAlertBySession(@PathVariable String sessionId){
        return ApiResponse.<List<AlertResponse>>builder()
                .code(200)
                .message("Lấy danh sách thành công")
                .data(alertService.getAlertsBySession(Long.valueOf(sessionId)))
                .build();
    }

}
