package DATN.example.demo.service;

import DATN.example.demo.dto.response.DashboardSummaryResponse;
import DATN.example.demo.entity.User;
import DATN.example.demo.repository.AlertRepository;
import DATN.example.demo.repository.NotificationRepository;
import DATN.example.demo.repository.SessionRepository;
import DATN.example.demo.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class DashboardService {
    UserRepository userRepository;
    SessionRepository sessionRepository;
    AlertRepository alertRepository;
    NotificationRepository notificationRepository;

    public DashboardSummaryResponse getSummary(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));
        long totalSessions = sessionRepository.countByUserUserId(user.getUserId());
        double badPostureDuration = sessionRepository.sumTotalBadDurationByUserUserId(user.getUserId());
        long totalAlerts = alertRepository.countBySessionUserUserId(user.getUserId());
        long totalNotifications = notificationRepository.countByUserUserId(user.getUserId());
        return DashboardSummaryResponse.builder()
                .totalSessions(totalSessions)
                .badPostureDuration(badPostureDuration)
                .totalAlerts(totalAlerts)
                .totalNotifications(totalNotifications)
                .build();

    }
}
