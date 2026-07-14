package DATN.example.demo.service;

import DATN.example.demo.dto.request.AlertRequest;
import DATN.example.demo.dto.response.AlertResponse;
import DATN.example.demo.entity.Alert;
import DATN.example.demo.entity.Session;
import DATN.example.demo.enums.Status;
import DATN.example.demo.repository.AlertRepository;
import DATN.example.demo.repository.SessionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AlertService {
    AlertRepository alertRepository;
    SessionRepository sessionRepository;
    NotificationService notificationService;

    public String createAlert(AlertRequest alertRequest){
        Session session = sessionRepository.findById(alertRequest.getSessionId())
                .orElseThrow(() -> new RuntimeException("Session không tồn tại"));
        Alert alert = Alert.builder()
                .message(alertRequest.getMessage())
                .postureStatus(alertRequest.getPostureStatus())
                .createdAt(LocalDateTime.now())
                .session(session)
                .build();
        alertRepository.save(alert);

        //Tạo thông báo send to user
        switch (alertRequest.getPostureStatus()){
            case Status.WARNING_POSTURE -> {
                notificationService.createNotification(session.getUser(),
                        "Cảnh báo tư thế",
                        "Tư thế ngồi của bạn đang có dấu hiệu không tốt, vui lòng điều chỉnh tư thế",
                        "WARNING"
                        );
            }

            case Status.BAD_POSTURE -> {
                notificationService.createNotification(session.getUser(),
                        "Cảnh báo tư thế",
                        "Bạn ngồi sai tư thế quá 30 giây",
                        "ALERT"
                );
            }
        }
        return "Tạo alert thành công";
    }

    public List<AlertResponse> getAlertsBySession(Long sessionId){
        return alertRepository.findBySessionSessionIdOrderByCreatedAtDesc(sessionId)
                .stream()
                .map(alert -> AlertResponse.builder()
                        .alertId(alert.getAlertId())
                        .message(alert.getMessage())
                        .postureStatus(alert.getPostureStatus())
                        .createdAt(alert.getCreatedAt())
                        .build()
                )
                .toList();
    }
}
