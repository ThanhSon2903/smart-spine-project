package DATN.example.demo.service;


import DATN.example.demo.dto.request.PostureSnapshotRequest;
import DATN.example.demo.dto.response.PostureResponse;
import DATN.example.demo.entity.PostureSnapshot;
import DATN.example.demo.entity.Session;
import DATN.example.demo.enums.Status;
import DATN.example.demo.mapper.PostureSnapshotMapper;
import DATN.example.demo.repository.PostureSnapshotRepository;
import DATN.example.demo.repository.SessionRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PostureSnapshotService {

    SessionRepository sessionRepository;
    PostureSnapshotRepository postureSnapshotRepository;
    PostureSnapshotMapper postureSnapshotMapper;
    SimpMessagingTemplate messagingTemplate;

    public String createPostureSnapshot(PostureSnapshotRequest request) {
        Session session = sessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new RuntimeException("Session không tồn tại"));

        PostureSnapshot postureSnapshot =
                PostureSnapshot.builder()
                        .session(session)
                        .shouterRatio(request.getShoulderRatio())
                        .neckAngle(request.getNeckAngle())
                        .torsoAngle(request.getTorsoAngle())
                        .status(request.getPostureStatus())
                        .createdAt(LocalDateTime.now())
                        .build();

        postureSnapshotRepository.save(postureSnapshot);
        if(request.getPostureStatus() == Status.BAD_POSTURE){
            Long currentBadDuration = (session.getTotalBadDuration() == null ? 0L : session.getTotalBadDuration());
            session.setTotalBadDuration(currentBadDuration + 5);
            sessionRepository.save(session);
        }

        String topic = "/topic/session/" + request.getSessionId();
        messagingTemplate.convertAndSend(topic, request);
        return "Lưu trạng thái thành công";
    }

    public List<PostureResponse> getAllPostureInSession(Long Id){
        List<PostureSnapshot> lst = postureSnapshotRepository.findBySessionSessionIdOrderByCreatedAtAsc(Id);
        return lst.stream()
                .map(postureSnapshotMapper::toPostureResponse)
                .toList();
    }

    @Transactional
    public void deleteSnapshot(Long id){
        postureSnapshotRepository.deleteBySessionSessionId(id);
    }
}
