package DATN.example.demo.config;


import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = false)
public class MqttConfig {

    @Value("${spring.mqtt.broker}")
    String broker;

    @Value("${spring.mqtt.client-id}")
    String clientId;

    @Bean
    public MqttClient mqttClient() throws Exception{ //Khởi tạo và kết nối tơí MQTT Broker
        MqttClient client = new MqttClient(broker,clientId);//Initialize the MQTT Client

        MqttConnectOptions options = new MqttConnectOptions();

        options.setAutomaticReconnect(true); //Tự động kết nối lại.
        options.setCleanSession(true); //Xoá đi các dữ liệu cũ khi kết nôí

        client.connect(options);
        return client;
    }
}
