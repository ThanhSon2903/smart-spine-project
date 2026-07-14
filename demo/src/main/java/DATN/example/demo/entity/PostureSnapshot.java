package DATN.example.demo.entity;


import DATN.example.demo.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_posture_snapshot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostureSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long postureSnapshotId;

    @Enumerated(EnumType.STRING)
    Status status;  

    double shouterRatio;
    double torsoAngle;
    double neckAngle;

    LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "session_id")
    Session session;
}
