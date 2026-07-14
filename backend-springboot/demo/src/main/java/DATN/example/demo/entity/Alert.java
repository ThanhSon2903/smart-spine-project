package DATN.example.demo.entity;


import DATN.example.demo.enums.AlertType;
import DATN.example.demo.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_alert")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long alertId;

    String message;

    @Enumerated(EnumType.STRING)
    Status postureStatus;

    LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "session_id")
    Session session;
}
