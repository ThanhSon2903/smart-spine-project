package DATN.example.demo.mapper;

import DATN.example.demo.dto.response.NotificationResponse;
import DATN.example.demo.entity.Notification;
import ch.qos.logback.core.model.ComponentModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationResponse toNotificationResponse(Notification notification);

}
