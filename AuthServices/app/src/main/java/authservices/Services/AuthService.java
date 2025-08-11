package authservices.Services;

import authservices.DTO.AuthResponse;
import authservices.DTO.LoginRequest;
import authservices.DTO.RegisterRequest;
import authservices.Repository.UserRepository;
import authservices.entities.Role;
import authservices.entities.UserInfo;
import authservices.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest req){
        if(userRepository.existsByUserName(req.getUsername())){
            throw new RuntimeException("Username already exists!");
        }
        UserInfo user = UserInfo.builder().username(req.getUsername()).password(passwordEncoder.encode(req.getPassword())).role(Role.USER).build();
        userRepository.save(user);

        String accessToken = jwtService.generateToken(null,user.getUsername());
        String refreshToken = jwtService.generateRefreshToken(user.getUsername());

        return new AuthResponse(accessToken,refreshToken);
    }
    public AuthResponse login(LoginRequest req){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(),req.getPassword())
        );

        UserInfo user = userRepository.findByUserName(req.getUsername()).orElseThrow(()->new RuntimeException(("User not found")));
        String accessToken = jwtService.generateToken(null, user.getUsername());
        String refreshToken = jwtService.generateRefreshToken(user.getUsername());

        return new AuthResponse(accessToken, refreshToken);


    }

    public AuthResponse refreshToken(String refreshToken){
        String username = jwtService.extractUsername(refreshToken);
        if(!jwtService.isTokenValid(refreshToken,username)){
            throw new RuntimeException("Invalid refresh token");
        }
        String newAccessToken = jwtService.generateToken(null,username);
        return new AuthResponse(newAccessToken,refreshToken);
    }

}
