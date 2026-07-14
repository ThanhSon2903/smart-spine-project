package DATN.example.demo.dto.response;

import DATN.example.demo.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AlertResponse {

    Long alertId;
    String message;
    Status postureStatus;
    LocalDateTime createdAt;
}
