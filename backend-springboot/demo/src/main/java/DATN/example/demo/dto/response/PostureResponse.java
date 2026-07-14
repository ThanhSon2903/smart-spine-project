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
public class PostureResponse {
    Long postureSnapshotId;
    double shouterRatio;
    double torsoAngle;
    double neckAngle;
    Status status;
    LocalDateTime createdAt;
}
