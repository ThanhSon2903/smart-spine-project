package DATN.example.demo.repository;

import DATN.example.demo.entity.PostureSnapshot;
import DATN.example.demo.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostureSnapshotRepository extends JpaRepository<PostureSnapshot,Long> {

    List<PostureSnapshot> findBySession(Session session);

    List<PostureSnapshot> findBySessionSessionId(Long sessionId);

    Optional<PostureSnapshot> findTopBySessionOrderByCreatedAtDesc(Session session);

    List<PostureSnapshot> findBySessionSessionIdOrderByCreatedAtAsc(Long sessionId);

    void deleteBySessionSessionId(Long id);

}
