package ra.edu.service;

import ra.edu.dto.request.LoginRequest;
import ra.edu.dto.request.RegisterRequest;
import ra.edu.dto.response.AuthResponse;
import ra.edu.dto.response.UserProfileResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    UserProfileResponse getCurrentProfile();
    void logout(String token);
}
