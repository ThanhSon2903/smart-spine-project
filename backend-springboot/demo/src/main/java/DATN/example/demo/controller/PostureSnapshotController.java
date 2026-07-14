package DATN.example.demo.controller;


import DATN.example.demo.dto.request.PostureSnapshotRequest;
import DATN.example.demo.dto.response.PostureResponse;
import DATN.example.demo.service.PostureSnapshotService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posture-snapshots")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PostureSnapshotController {

    PostureSnapshotService postureSnapshotService;

    @PostMapping("/create")
    public ApiResponse<String> createPostureSnapshot(@RequestBody PostureSnapshotRequest request){
        System.out.println("REQUEST RECEIVED");
        System.out.println(request);
        return ApiResponse.<String>builder()
                .code(200)
                .message("Tạo hoàn tất")
                .data(postureSnapshotService.createPostureSnapshot(request))
                .build();
    }

    @GetMapping("/{sessionId}")
    public ApiResponse<List<PostureResponse>> getAllPostureInSession(@PathVariable String sessionId){
        return ApiResponse.<List<PostureResponse>>builder()
                .code(200)
                .message("Lấy về thành công")
                .data(postureSnapshotService.getAllPostureInSession(Long.valueOf(sessionId)))
                .build();
    }

    @DeleteMapping("/{sessionId}/delete")
    public ApiResponse<?> deletePosture(@PathVariable String sessionId){
        postureSnapshotService.deleteSnapshot(Long.valueOf(sessionId));
        return ApiResponse.builder()
                .code(200)
                .message("OK")
                .build();
    }
}
