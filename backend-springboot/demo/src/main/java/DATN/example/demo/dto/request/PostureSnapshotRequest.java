package DATN.example.demo.dto.request;


import DATN.example.demo.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostureSnapshotRequest {

    Long sessionId;
    Double shoulderRatio;
    Double torsoAngle;
    Double neckAngle;
    Status postureStatus;


}
