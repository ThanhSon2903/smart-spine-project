package DATN.example.demo.controller;


import DATN.example.demo.dto.response.NotificationResponse;
import DATN.example.demo.entity.User;
import DATN.example.demo.repository.UserRepository;
import DATN.example.demo.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class NotificationController {

    NotificationService notificationService;
    UserRepository userRepository;


    //API danh dau la da doc
    @PatchMapping("/{notificationId}/read")
    public ApiResponse<String> maskAsRead(@PathVariable String id){
        return ApiResponse.<String>builder()
                .code(200)
                .message("Success")
                .data(notificationService.maskAsRead(Long.valueOf(id)))
                .build();
    }


    //API lay ra xem bao nhieu thong bao chua doc
    @GetMapping("/unread-count")
    public ApiResponse<Long> countUnRead(){
        return ApiResponse.<Long>builder()
                .code(200)
                .message("Success")
                .data(notificationService.countUnRead())
                .build();
    }


    //API lay ra danh sach thong bao
    @GetMapping("/get-my-notifications")
    public ApiResponse<List<NotificationResponse>> getMyNotification(){
        return ApiResponse.<List<NotificationResponse>>builder()
                .code(200)
                .message("Lấy danh sách thành công")
                .data(notificationService.getMyNotification())
                .build();
    }
}
