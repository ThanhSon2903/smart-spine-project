package DATN.example.demo.repository;

import DATN.example.demo.entity.Alert;
import DATN.example.demo.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert,Long> {
    List<Alert> findBySession(Session session);
    List<Alert> findBySessionSessionId(Long sessionId);
    List<Alert> findBySessionSessionIdOrderByCreatedAtDesc(Long sessionId);
    Long countBySessionUserUserId(Long userId);
}
