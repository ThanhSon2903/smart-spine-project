package DATN.example.demo.dto.response;

import DATN.example.demo.entity.PostureSnapshot;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ViewDetailSessionResponse {
    Long sessionId;
    LocalDateTime startTime;
    LocalDateTime endTime;
    String duration;
    Long badPostureDuration;
    List<PostureResponse> postureResponses;
}
