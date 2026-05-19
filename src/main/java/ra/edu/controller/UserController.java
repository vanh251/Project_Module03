package ra.edu.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.request.UpdateRoleRequest;
import ra.edu.dto.request.UpdateStatusRequest;
import ra.edu.dto.request.UserCreateRequest;
import ra.edu.dto.request.UserUpdateRequest;
import ra.edu.dto.request.ChangePasswordRequest;
import ra.edu.dto.response.ApiResponse;
import ra.edu.dto.response.PageResponse;
import ra.edu.dto.response.UserProfileResponse;
import ra.edu.entity.Role;
import ra.edu.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import ra.edu.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ApiResponse<PageResponse<UserProfileResponse>> getAllUser(
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size
            ){
        Page<UserProfileResponse> userPage = userService.getAllUsers(role, isActive, page, size);
        return ApiResponse.paginated("Danh sách người dùng: ", userPage.getContent(), page, size, userPage.getTotalElements());
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserProfileResponse> getUserById(@PathVariable Integer userId){
        return ApiResponse.success("Thông tin người dùng: ", userService.getUserById(userId));
    }

    @PostMapping
    public ApiResponse<UserProfileResponse> createUser(@Valid @RequestBody UserCreateRequest request){
        return ApiResponse.success("Tạo người dùng thành công: ", userService.createUser(request));
    }

    @PutMapping("/{userId}/role")
    public ApiResponse<UserProfileResponse> updateRole(@PathVariable Integer userId,
                                                      @Valid @RequestBody UpdateRoleRequest updateRoleRequest){
        return ApiResponse.success("Cập nhật vai trò thành công: ", userService.updateRole(userId, updateRoleRequest));
    }

    @PutMapping("/{userId}/status")
    public ApiResponse<UserProfileResponse> toggleUserStatus(@PathVariable Integer userId,
                                                             @Valid @RequestBody UpdateStatusRequest request){
        return ApiResponse.success("Cập nhật trạng thái tài khoản thành công: ", userService.toggleUserStatus(userId, request));
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<Void> deleteUser(@PathVariable Integer userId){
        userService.deleteUser(userId);
        return ApiResponse.success("Xóa người dùng thành công", null);
    }

    @PutMapping("/{userId}")
    public ApiResponse<UserProfileResponse> updateProfile(
            @PathVariable Integer userId,
            @Valid @RequestBody UserUpdateRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ApiResponse.success("Cập nhật thông tin thành công", userService.updateProfile(userId, request, currentUser));
    }

    @PutMapping("/{userId}/password")
    public ApiResponse<Void> changePassword(
            @PathVariable Integer userId,
            @Valid @RequestBody ChangePasswordRequest request,
            @AuthenticationPrincipal User currentUser) {
        userService.changePassword(userId, request, currentUser);
        return ApiResponse.success("Đổi mật khẩu thành công", null);
    }
}
