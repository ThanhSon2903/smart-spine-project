package DATN.example.demo.entity;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class AIRuntime {

    private String token;
    private String refreshToken;
    private Long sessionId;

    public synchronized void update(String token, Long sessionId){
        this.token = token;
        this.sessionId = sessionId;
    }
}
