package DATN.example.demo.service;


import DATN.example.demo.dto.response.NotificationResponse;
import DATN.example.demo.entity.Notification;
import DATN.example.demo.entity.User;
import DATN.example.demo.mapper.NotificationMapper;
import DATN.example.demo.repository.NotificationRepository;
import DATN.example.demo.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cglib.proxy.Factory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class NotificationService {

    NotificationRepository notificationRepository;
    NotificationMapper notificationMapper;
    SimpMessagingTemplate messagingTemplate;
    UserRepository userRepository;


    //Tao thong bao
    public void createNotification(User user,String title,String message,String type){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Notification notification = Notification.builder()
                .title(title)
                .message(message)
                .type(type)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();


        notificationRepository.save(notification);

        NotificationResponse notificationResponse =  notificationMapper.toNotificationResponse(notification);

        messagingTemplate.convertAndSend(
                "topic/notifications" + user.getUserId(),
                notificationResponse
        );
    }

    //Đánh dấu đã đọc
    public String maskAsRead(Long notificationId){
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification không tồn tại"));
        notification.setRead(true);
        notificationRepository.save(notification);
        return "Đã đọc thông báo";
    }

    //Đếm số thông báo chưa đọc
    public Long countUnRead(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        return notificationRepository.countByUserUserIdAndIsReadFalse(user.getUserId());

    }


    //Lấy danh sách notification
    public List<NotificationResponse> getMyNotification(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        return notificationRepository.findByUserUserIdOrderByCreatedAtDesc(user.getUserId())
                .stream()
                .map(notificationMapper::toNotificationResponse)
                .toList();
    }

}
