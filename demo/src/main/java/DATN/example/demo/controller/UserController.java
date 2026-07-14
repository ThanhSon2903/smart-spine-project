package DATN.example.demo.controller;

import DATN.example.demo.dto.request.AuthRequest;
import DATN.example.demo.dto.request.ChangePassword;
import DATN.example.demo.dto.request.RegisterRequest;
import DATN.example.demo.dto.request.VerifyOtpRequest;
import DATN.example.demo.dto.response.AuthResponse;
import DATN.example.demo.dto.response.UserResponse;
import DATN.example.demo.repository.UserRepository;
import DATN.example.demo.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class UserController {

    UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ApiResponse<String> registerUser(@RequestBody RegisterRequest registerRequest){
        return ApiResponse.<String>builder()
                .code(200)
                .message("Vui lòng xác thực OTP")
                .data(userService.register(registerRequest))
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> loginUser(@RequestBody AuthRequest authRequest){
        return ApiResponse.<AuthResponse>builder()
                .code(200)
                .message("Đăng nhập thành công !")
                .data(userService.login(authRequest))
                .build();
    }

    @PostMapping("/resent-otp/{email}")
    public ApiResponse<String> resentOtp(@PathVariable String email){
        return ApiResponse.<String>builder()
                .code(200)
                .message("Vui lòng xác thực OTP")
                .data(userService.resentOTP(email))
                .build();
    }
    @PostMapping("/verify-otp")
    public ApiResponse<String> verifyOtp(@RequestBody VerifyOtpRequest verifyOtpRequest){
        return ApiResponse.<String>builder()
                .code(200)
                .message("Đăng ký thành công")
                .data(userService.verifyOtp(verifyOtpRequest))
                .build();
    }

    @GetMapping("/get/me")
    public ApiResponse<UserResponse> getMyInfo(Authentication authentication){
        String username = authentication.getName();
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("Lấy thông tin thành công")
                .data(userService.getUserInfo(username))
                .build();
    }

    @GetMapping("/get/all")
    public ApiResponse<List<UserResponse>> getListUsers(){
        List<UserResponse> lst = userService.getAll();
        return ApiResponse.<List<UserResponse>>builder()
                .code(200)
                .message("Lấy thông tin thành công")
                .data(lst)
                .build();
    }

    @GetMapping("/get/{userId}")
    public ApiResponse<UserResponse> getUserById(@PathVariable String userId){
        UserResponse userResponse = userService.getUserById(userId);
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("Lấy thông tin thành công")
                .data(userResponse)
                .build();
    }

    @DeleteMapping("/delete/{userId}")
    public Map<Integer,String> deleteUserById(@PathVariable String userId){
        return userService.deleteUserById(userId);
    }

    @PutMapping("/change/password")
    public ApiResponse<String> changePassword(@RequestBody ChangePassword changePassword,
                                              Authentication authentication){
        userService.changePassword(changePassword, authentication.getName());
        return ApiResponse.<String>builder()
                .code(200)
                .message("Đổi mật khẩu thành công")
                .data("OK")
                .build();
    }


    @GetMapping("/test")
    public String test(Authentication authentication){

        return authentication.getAuthorities().toString();
    }

}
