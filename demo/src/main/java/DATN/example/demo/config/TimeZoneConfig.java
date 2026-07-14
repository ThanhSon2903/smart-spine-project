package DATN.example.demo.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.propertyeditors.TimeZoneEditor;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class TimeZoneConfig {

    @PostConstruct
    public void init(){
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        System.out.println("Current TimeZone: " + TimeZone.getDefault().getID());
    }
}
