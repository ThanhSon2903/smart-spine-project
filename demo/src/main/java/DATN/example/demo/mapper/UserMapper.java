package DATN.example.demo.mapper;

import DATN.example.demo.dto.request.RegisterRequest;
import DATN.example.demo.dto.response.UserResponse;
import DATN.example.demo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "userId",ignore = true)
    @Mapping(target = "role",ignore = true)
    @Mapping(target = "createdAt",ignore = true)
    @Mapping(target = "sessions",ignore = true)
    @Mapping(target = "notifications",ignore = true)
    @Mapping(target = "userProfile",ignore = true)
    User toUser(RegisterRequest registerRequest);


    UserResponse toUserResponse(User user);


}
