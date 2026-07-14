package DATN.example.demo.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationResponse {

    Long notificationId;
    String title;
    String message;
    String type;
    Boolean isRead;
    LocalDateTime createdAt;

}
