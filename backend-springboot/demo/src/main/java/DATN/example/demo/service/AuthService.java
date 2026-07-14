package DATN.example.demo.service;

import DATN.example.demo.dto.request.RefreshTokenRequest;
import DATN.example.demo.dto.response.RefreshTokenResponse;
import DATN.example.demo.entity.User;
import DATN.example.demo.repository.UserRepository;
import DATN.example.demo.utils.JwtUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AuthService {

    JwtUtils jwtUtils;
    UserRepository userRepository;
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request){
        String refreshToken = request.getRefreshToken();
        String username = jwtUtils.extractUsername(refreshToken);
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user: " + username));

        if(!jwtUtils.isValidToken(refreshToken,user)){
            throw new RuntimeException("Token không hợp lệ");
        }
        String newAccessToken = jwtUtils.generateToken(user);
        return RefreshTokenResponse.builder()
                .accessToken(newAccessToken)
                .build();
    }

}
