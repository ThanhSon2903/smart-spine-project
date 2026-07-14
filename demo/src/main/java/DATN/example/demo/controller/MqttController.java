package DATN.example.demo.controller;


import DATN.example.demo.dto.request.MqttRequest;
import DATN.example.demo.service.MqttPublisherService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import static DATN.example.demo.enums.Status.GOOD_POSTURE;

@RestController
@RequestMapping("/mqtt")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class MqttController {

    MqttPublisherService mqttPublisherService;


    @GetMapping("/test")
    public String testMqtt() {

        MqttRequest request = new MqttRequest();
        request.setPlayVoice(false);
        request.setStatus(GOOD_POSTURE);

        mqttPublisherService.sendWarning(request);

        return "MQTT SENT OK";
    }

    @PostMapping("/alert")
    public ApiResponse<?> alert(
            @RequestBody MqttRequest mqttRequest
    ) {

        mqttPublisherService.sendWarning(
            mqttRequest
        );

        return ApiResponse.builder()
                .code(200)
                .message("MQTT sent message")
                .build();
    }


}
