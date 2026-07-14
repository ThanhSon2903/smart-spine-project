package DATN.example.demo.service;


import DATN.example.demo.dto.request.MqttRequest;
import DATN.example.demo.enums.Status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class MqttPublisherService {

    MqttClient mqttClient;
    ObjectMapper objectMapper;

    public void sendWarning(MqttRequest mqttRequest){
        String topic = "posture/alert";

        try {
            String json = objectMapper.writeValueAsString(mqttRequest);
            MqttMessage mqttMessage = new MqttMessage(json.getBytes());
            mqttMessage.setQos(1);
            mqttClient.publish(topic,mqttMessage);
            System.out.println("Message done.");
        }
        catch (MqttException e){
            System.out.print("Reason code: " + e.getReasonCode());
            e.printStackTrace();

        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
