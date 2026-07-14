package DATN.example.demo.service;


import DATN.example.demo.dto.response.*;
import DATN.example.demo.entity.AIRuntime;
import DATN.example.demo.entity.PostureSnapshot;
import DATN.example.demo.entity.Session;
import DATN.example.demo.entity.User;
import DATN.example.demo.repository.PostureSnapshotRepository;
import DATN.example.demo.repository.SessionRepository;
import DATN.example.demo.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class SessionService {

    SessionRepository sessionRepository;
    UserRepository userRepository;
    AIRuntime aiRuntime;
    AIService aiService;
    PostureSnapshotRepository postureSnapshotRepository;

    public SessionResponse startSession(String email,String token){
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy user!"));

            Optional<Session> active = sessionRepository.findByUserAndEndTimeIsNull(user);
            if(active.isPresent()){
                throw new RuntimeException("Bạn có đang có Session hoạt động");
            }

            Session session = Session.builder()
                    .user(user)
                    .startTime(LocalDateTime.now())
                    .totalBadDuration(0L)
                    .build();

            sessionRepository.save(session);

            aiRuntime.update(token, session.getSessionId());
            aiService.startAI();

            return SessionResponse.builder()
                    .sessionId(session.getSessionId())
                    .startTime(session.getStartTime())
                    .build();

    }

    public String endSession(Long sessionId,String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session kh tồn tại"));

        if(!session.getUser().getUserId().equals(user.getUserId())){
            throw new RuntimeException("Bạn không có quyền kết thúc Session này");
        }

        if(session.getEndTime() != null){
            throw new RuntimeException("Session đã kết thúc");
        }
        session.setEndTime(LocalDateTime.now());
        aiService.stopAI();
        sessionRepository.save(session);
        return "Kết thúc phiên thành công";
    }

    public ActiveSessionResponse getActiveSession(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        Session session = sessionRepository.findByUserAndEndTimeIsNull(user)
                .orElseThrow(() -> new RuntimeException("Không có session đang hoạt động"));
        return ActiveSessionResponse.builder()
                .sessionId(session.getSessionId())
                .startTime(session.getStartTime())
                .endTime(session.getEndTime())
                .totalBadDuration(session.getTotalBadDuration())
                .build();
    }

    public Page<SessionResponse> getAllSessions(String email, int page, int size){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        Pageable pageable = PageRequest.of(page, size, Sort.by("sessionId").descending());
        Page<Session> sessionPage = sessionRepository.findByUser(user,pageable);
        return sessionPage.map(session -> SessionResponse.builder()
                .sessionId(session.getSessionId())
                .startTime(session.getStartTime())
                .build());
    }

    //Lay ve danh sach cac phien lam viec
    public List<ListSessionResponse> listSessionResponse(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        List<Session>list = sessionRepository.findByUser(user);
        if(list.isEmpty()){
            throw new RuntimeException("Không tồn tại danh sách phiên làm việc");
        }
        List<ListSessionResponse>lst = list.stream()
                .map(session -> {
                    ListSessionResponse listSessionResponse = new ListSessionResponse();

                    listSessionResponse.setSessionId(session.getSessionId());
                    listSessionResponse.setStartTime(session.getStartTime());
                    listSessionResponse.setEndTime(session.getEndTime());

                    if(session.getEndTime() != null){
                        Duration duration = Duration.between(
                                session.getStartTime(),
                                session.getEndTime());
                        long minutes = duration.toMinutes();
                        long seconds = duration.getSeconds() % 60;
                        listSessionResponse.setDuration(minutes + " phút" + seconds + " giây");
                    }

                    else listSessionResponse.setDuration("Đang theo dõi");
                    listSessionResponse.setBadPostureDuration(session.getTotalBadDuration());
                    return listSessionResponse;
                })
                .toList();
        return lst;
    }

    //Lay danh sach cac tu the da phat hien duoc trong 1 phien lam viec
    public ViewDetailSessionResponse getViewDetail(Long sessionId){
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session không tồn tại"));

        List<PostureSnapshot> snapshots = postureSnapshotRepository.findBySessionSessionId(session.getSessionId());
        List<PostureResponse> postureResponses = snapshots.stream()
                .map(snapshot -> {
                    PostureResponse response = new PostureResponse();
                    response.setPostureSnapshotId(snapshot.getPostureSnapshotId());
                    response.setShouterRatio(snapshot.getShouterRatio());
                    response.setTorsoAngle(snapshot.getTorsoAngle());
                    response.setNeckAngle(snapshot.getNeckAngle());
                    response.setStatus(snapshot.getStatus());
                    response.setCreatedAt(snapshot.getCreatedAt());
                    return response;
                })
                .toList();

        ViewDetailSessionResponse response = new ViewDetailSessionResponse();
        response.setSessionId(session.getSessionId());
        response.setStartTime(session.getStartTime());
        response.setEndTime(session.getEndTime());
        response.setBadPostureDuration(session.getTotalBadDuration());
        if(session.getEndTime() != null){
            Duration duration = Duration.between(
                    session.getStartTime(),
                    session.getEndTime()
            );
            response.setDuration(duration.toMinutes() + " phút");
        }
        else{
            response.setDuration("Đang theo dõi");
        }
        response.setPostureResponses(postureResponses);
        return response;
    }
}
