package DATN.example.demo.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ActiveSessionResponse {

    Long sessionId;
    LocalDateTime startTime;
    LocalDateTime endTime;
    Long totalBadDuration;
}
