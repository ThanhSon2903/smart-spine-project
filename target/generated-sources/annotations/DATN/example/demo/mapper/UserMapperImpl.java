package DATN.example.demo.mapper;

import DATN.example.demo.dto.request.RegisterRequest;
import DATN.example.demo.dto.response.UserResponse;
import DATN.example.demo.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-07T13:44:18+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.9 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toUser(RegisterRequest registerRequest) {
        if ( registerRequest == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.username( registerRequest.getUsername() );
        user.password( registerRequest.getPassword() );
        user.email( registerRequest.getEmail() );

        return user.build();
    }

    @Override
    public UserResponse toUserResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.userId( user.getUserId() );
        userResponse.username( user.getUsername() );
        userResponse.email( user.getEmail() );
        userResponse.role( user.getRole() );
        userResponse.createdAt( user.getCreatedAt() );

        return userResponse.build();
    }
}
