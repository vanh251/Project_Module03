package ra.edu.service;

import org.springframework.data.domain.Page;
import ra.edu.dto.request.UpdateRoleRequest;
import ra.edu.dto.request.UpdateStatusRequest;
import ra.edu.dto.request.UserCreateRequest;
import ra.edu.dto.response.UserProfileResponse;
import ra.edu.entity.Role;

public interface UserService {
    Page<UserProfileResponse> getAllUsers(Role role, Boolean isActive, int page, int size);
    UserProfileResponse getUserById(Integer userId);
    UserProfileResponse createUser(UserCreateRequest userCreateRequest);
    UserProfileResponse updateRole(Integer userId, UpdateRoleRequest updateRoleRequest);
    UserProfileResponse toggleUserStatus(Integer userId, UpdateStatusRequest request);
    void deleteUser(Integer userId);
}
