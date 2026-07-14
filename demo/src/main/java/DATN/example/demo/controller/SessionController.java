package DATN.example.demo.controller;


import DATN.example.demo.dto.response.*;
import DATN.example.demo.entity.AIRuntime;
import DATN.example.demo.service.SessionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class SessionController {

    SessionService sessionService;


    @PostMapping("/start")
    public ApiResponse<SessionResponse> startSession(Authentication authentication,
                                                     @RequestHeader("Authorization") String authorization
                                                     ){
        String email = authentication.getName();
        String token = authorization.substring(7);
        SessionResponse sessionResponse = sessionService.startSession(email,token);
        return ApiResponse.<SessionResponse>builder()
                .code(200)
                .message("Phiên theo dõi đã được khởi tạo")
                .data(sessionResponse)
                .build();
    }

    @PostMapping("/{sessionId}/end")
    public ApiResponse<String> endSession(@PathVariable Long sessionId, Authentication authentication){
        String email = authentication.getName();
        return ApiResponse.<String>builder()
                .code(200)
                .message("Phiên theo dõi đã được kết thúc")
                .data(sessionService.endSession(sessionId,email))
                .build();
    }

    @GetMapping("/active")
    public ApiResponse<ActiveSessionResponse> getActiveSession(Authentication authentication){
        return ApiResponse.<ActiveSessionResponse>builder()
                .code(200)
                .message("Lấy về thành công phiên đang hoạt đông")
                .data(sessionService.getActiveSession(authentication.getName()))
                .build();
    }

    @GetMapping("/periods")
    public ApiResponse<Page<SessionResponse>> getAllPeriods(Authentication authentication,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size){
        String email = authentication.getName();
        Page<SessionResponse> sessions = sessionService.getAllSessions(email, page, size);
        return ApiResponse.<Page<SessionResponse>>builder()
                .code(200)
                .message("Lấy danh sách các phiên làm việc thành công")
                .data(sessions)
                .build();
    }

    @GetMapping("/list-sessions")
    public ApiResponse<List<ListSessionResponse>> getSessions(Authentication authentication){
        String email = authentication.getName();
        return ApiResponse.<List<ListSessionResponse>>builder()
                .code(200)
                .message("Lấy danh sách phiên làm viêc thành công")
                .data(sessionService.listSessionResponse(email))
                .build();
    }

    @GetMapping("/view-detail/{sessionId}")
    public ApiResponse<ViewDetailSessionResponse> getView(@PathVariable String sessionId){
        return ApiResponse.<ViewDetailSessionResponse>builder()
                .code(200)
                .message(("Lấy chi tiết danh sách các tư thế trong  phiên làm việc thành công"))
                .data(sessionService.getViewDetail(Long.valueOf(sessionId)))
                .build();
    }
}
