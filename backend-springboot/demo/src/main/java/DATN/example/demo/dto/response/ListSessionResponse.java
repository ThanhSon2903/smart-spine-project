package DATN.example.demo.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListSessionResponse {
    Long sessionId;
    LocalDateTime startTime;
    LocalDateTime endTime;
    String duration;
    Long badPostureDuration;
}
