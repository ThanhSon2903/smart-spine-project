package DATN.example.demo.dto.response;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class AIService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String AI_URL = "https://circling-tradition-twitch.ngrok-free.dev";

    public void startAI(){

        restTemplate.postForObject(
                AI_URL + "/start",
                null,
                String.class
        );

    }


    public void stopAI(){

        restTemplate.postForObject(
                AI_URL + "/stop",
                null,
                String.class
        );

    }
}
