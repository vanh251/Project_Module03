package ra.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.request.LoginRequest;
import ra.edu.dto.request.RegisterRequest;
import ra.edu.dto.response.ApiResponse;
import ra.edu.dto.response.AuthResponse;
import ra.edu.dto.response.UserProfileResponse;
import ra.edu.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ApiResponse.success("Đăng ký thành công", authService.register(request));
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success("Đăng nhập thành công", authService.login(request));
    }

    @PostMapping("/verify")
    public ApiResponse<Boolean> verifyToken() {
        return ApiResponse.success("Token hợp lệ", true);
    }

    @GetMapping("/me")
    public ApiResponse<UserProfileResponse> getCurrentProfile() {
        return ApiResponse.success("Thông tin người dùng", authService.getCurrentProfile());
    }
}
