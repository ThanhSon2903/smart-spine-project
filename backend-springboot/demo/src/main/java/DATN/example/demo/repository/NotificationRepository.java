package DATN.example.demo.repository;

import DATN.example.demo.entity.Notification;
import DATN.example.demo.entity.User;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification> findByUser(User user);
    List<Notification> findByUserUserIdAndIsReadFalse(Long userId);
    Long countByUserUserIdAndIsReadFalse(Long userId);
    List<Notification> findByUserUserIdOrderByCreatedAtDesc(Long userId);
    Long countByUserUserId(Long userId);
}
