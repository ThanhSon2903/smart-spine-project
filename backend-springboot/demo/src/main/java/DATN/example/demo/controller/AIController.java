package DATN.example.demo.controller;


import DATN.example.demo.dto.response.AuthResponse;
import DATN.example.demo.entity.AIRuntime;
import DATN.example.demo.utils.JwtUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AIController {

    AIRuntime aiRuntime;
    JwtUtils jwtUtils;
    @GetMapping("/config")
    public ApiResponse<Map<String,Object>> config(){
//        if(jwtUtils.willExpireSoon(aiRuntime.getToken(),120)){
//            AuthResponse authResponse
//        }
        Map<String,Object> data = new HashMap<>();
        data.put("token",aiRuntime.getToken());
        data.put("sessionId",aiRuntime.getSessionId());
        return ApiResponse.<Map<String, Object>>builder()
                .code(200)
                .message("Gửi thành công token và sessionId thành công")
                .data(data)
                .build();
    }
}
