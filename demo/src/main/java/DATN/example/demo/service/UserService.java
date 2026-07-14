package DATN.example.demo.service;

import DATN.example.demo.dto.request.AuthRequest;
import DATN.example.demo.dto.request.ChangePassword;
import DATN.example.demo.dto.request.RegisterRequest;
import DATN.example.demo.dto.request.VerifyOtpRequest;
import DATN.example.demo.dto.response.AuthResponse;
import DATN.example.demo.dto.response.UserResponse;
import DATN.example.demo.entity.PendingUser;
import DATN.example.demo.entity.User;
import DATN.example.demo.enums.Role;
import DATN.example.demo.mapper.UserMapper;
import DATN.example.demo.repository.PendingUserRepository;
import DATN.example.demo.repository.UserRepository;
import DATN.example.demo.utils.JwtUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class UserService {


    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    PendingUserRepository pendingUserRepository;
    EmailOtpService emailOtpService;
    JwtUtils jwtUtils;

    public String register(RegisterRequest registerRequest){
        // Đã có tài khoản chính thức
        boolean existedEmail = userRepository.existsByEmail(registerRequest.getEmail());
        if(existedEmail){
            throw new RuntimeException("Người dùng đã tồn tại ");
        }
        boolean existedPendingUser = pendingUserRepository.existsByEmail(registerRequest.getEmail());
        // Kiểm tra email đang chờ xác thực
        if(existedPendingUser){
            return resentOTP(registerRequest.getEmail());
        }
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        PendingUser pendingUser = new PendingUser();
        pendingUser.setUsername(registerRequest.getUsername());
        pendingUser.setEmail(registerRequest.getEmail());
        pendingUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        pendingUser.setOtp(otp);
        pendingUser.setExpiredAt(LocalDateTime.now().plusMinutes(1));
        pendingUserRepository.save(pendingUser);
        emailOtpService.sentOtpEmail(registerRequest.getEmail(),otp);
        return "Đã gửi OTP đến Gmail: " + registerRequest.getEmail();
    }

    //Ham xac thuc OTP
    public String verifyOtp(VerifyOtpRequest verifyOtpRequest){
        PendingUser pendingUser = pendingUserRepository.findByEmail(verifyOtpRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Email chưa được đăng ký"));
        if(LocalDateTime.now().isAfter(pendingUser.getExpiredAt())){
            throw new RuntimeException("OTP đã hết hạn, vui lòng nhấn gửi lại");
        }
        if(!pendingUser.getOtp().equals(verifyOtpRequest.getOtp())){
            throw new RuntimeException("OTP không hợp lệ!");
        }
        User user = User.builder()
                .email(pendingUser.getEmail())
                .username(pendingUser.getUsername())
                .password(pendingUser.getPassword())
                .role(Role.USER)
                .build();
        userRepository.save(user);
        pendingUserRepository.delete(pendingUser);
        return "Xác thực tài khoản thành công!";
    }

    //Ham gui lai OTP
    public String resentOTP(String email){
        PendingUser pendingUser = pendingUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email chưa được đăng ký"));
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        pendingUser.setOtp(otp);
        pendingUser.setExpiredAt(LocalDateTime.now().plusMinutes(1));
        pendingUserRepository.save(pendingUser);
        emailOtpService.sentOtpEmail(email,otp);
        return "Gửi lại OTP thành công";

    }
    public AuthResponse login(AuthRequest authRequest){
        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Email không hợp lệ"));
        boolean authenticated = passwordEncoder.matches(authRequest.getPassword(),user.getPassword());
        if(!authenticated){
            throw new RuntimeException("Password không hợp lệ");
        }
        String access_token = jwtUtils.generateToken(user);
        String refresh_token = jwtUtils.generateRefreshToken(user);
        System.out.println(jwtUtils.extractUsername(access_token));
        return AuthResponse.builder()
                .accessToken(access_token)
                .refreshToken(refresh_token)
                .tokenType("Bearer")
                .build();
    }

    public Map<Integer,String> deleteUserById(String userId){
        userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("Không tìm thấy id"));
        userRepository.deleteById(Long.valueOf(userId));
        return Map.of(200, "Xoá thành công");
    }

    public UserResponse getUserInfo(String username){
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Người dùng không tìm thấy"));
        return userMapper.toUserResponse(user);
    }

    public List<UserResponse> getAll(){
        List<User> users= userRepository.findAll();
        return users.stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    public UserResponse getUserById(String userId){
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("Không tồn tại id này"));
        return userMapper.toUserResponse(user);
    }

    public void changePassword(ChangePassword cp, String username){
        System.out.print("username: " + username);
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Không tồn tại user"));

        boolean ok = passwordEncoder.matches(
                cp.getOldPassword(), user.getPassword()
        );

        if(!ok){
            throw new RuntimeException("Mật khẩu không đúng, vui lòng kiểm tra lại!");
        }
        user.setPassword(passwordEncoder.encode(cp.getNewPassword()));
        userRepository.save(user);
    }
}
