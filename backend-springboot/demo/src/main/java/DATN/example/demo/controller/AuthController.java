package DATN.example.demo.controller;


import DATN.example.demo.dto.request.RefreshTokenRequest;
import DATN.example.demo.dto.response.RefreshTokenResponse;
import DATN.example.demo.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//Xử lý cấp phát token mới
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AuthController {

    AuthService authService;

    @PostMapping("/refresh-token")
    public ApiResponse<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request){
        RefreshTokenResponse tokenResponse = authService.refreshToken(request);
        return ApiResponse.<RefreshTokenResponse>builder()
                .code(200)
                .message("Refresh Token thành công")
                .data(tokenResponse)
                .build();
    }


}
