package DATN.example.demo.repository;

import DATN.example.demo.entity.Session;
import DATN.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session,Long> {

    List<Session> findByUser(User user);
    List<Session> findByUserUserId(Long userId);
    Optional<Session> findByUserAndEndTimeIsNull(User user);
    Long countByUserUserId(Long id);
    Page<Session> findByUser(User user, Pageable pageable);

    @Query("""
    SELECT COALESCE(SUM(s.totalBadDuration), 0)
    FROM Session s
    WHERE s.user.userId = :userId
    """)

    Double sumTotalBadDurationByUserUserId(@Param("userId") Long id);
}
