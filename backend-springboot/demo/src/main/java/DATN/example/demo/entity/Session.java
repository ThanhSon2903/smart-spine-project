package DATN.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tbl_session")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long sessionId;

    @Column(nullable = false)
    LocalDateTime startTime;
    LocalDateTime endTime;

    Long totalBadDuration;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(mappedBy = "session")
    List<PostureSnapshot> postureSnapshotList;

    @OneToMany(mappedBy = "session")
    List<Alert> alertList;
}
 