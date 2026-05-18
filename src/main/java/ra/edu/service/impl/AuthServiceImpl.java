package ra.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ra.edu.config.exception.ResourceAlreadyExistsException;
import ra.edu.config.jwt.JwtService;
import ra.edu.dto.request.LoginRequest;
import ra.edu.dto.request.RegisterRequest;
import ra.edu.dto.response.AuthResponse;
import ra.edu.dto.response.UserProfileResponse;
import ra.edu.entity.Role;
import ra.edu.entity.User;
import ra.edu.mapper.UserMapper;
import ra.edu.repository.UserRepository;
import ra.edu.service.AuthService;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;


    @Override
    public AuthResponse register(RegisterRequest request) {
        if(userRepository.existsByUsername(request.getUsername())) {
            throw new ResourceAlreadyExistsException("Tên đăng nhập đã tồn tại");
        }

        User user = userMapper.toEntity(request);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.STUDENT); // Mặc định role là Student
        user.setIsActive(true);

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("username")) {
                throw new ResourceAlreadyExistsException("Tên đăng nhập đã tồn tại");
            } else if (e.getMessage().contains("email")) {
                throw new ResourceAlreadyExistsException("Email đã được đăng ký");
            }
            throw e;
        }
        String token = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng này: " + request.getUsername()));
        String token = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
    }

    @Override
    public UserProfileResponse getCurrentProfile() {
        String username = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng này: " + username));
        return userMapper.toUserProfileResponse(user);
    }

    @Override
    public void logout(String token) {
        jwtService.invalidateToken(token);
    }
}
