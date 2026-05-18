package ra.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ra.edu.config.exception.ForbiddenOperationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ra.edu.config.exception.ResourceNotFoundException;
import ra.edu.dto.request.UpdateRoleRequest;
import ra.edu.dto.request.UpdateStatusRequest;
import ra.edu.dto.request.UserCreateRequest;
import ra.edu.dto.request.UserUpdateRequest;
import ra.edu.dto.request.ChangePasswordRequest;
import ra.edu.dto.response.UserProfileResponse;
import ra.edu.config.exception.BadRequestException;
import ra.edu.config.exception.ResourceAlreadyExistsException;
import ra.edu.entity.Role;
import ra.edu.entity.User;
import ra.edu.mapper.UserMapper;
import ra.edu.repository.UserRepository;
import ra.edu.service.UserService;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<UserProfileResponse> getAllUsers(Role role, Boolean isActive, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<User> userPage = userRepository.findAllWithFilter(role, isActive, pageable);
        return userPage.map(userMapper :: toUserProfileResponse);
    }

    @Override
    public UserProfileResponse getUserById(Integer userId) {
        User user = userRepository.findById(userId.longValue())
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));
        return userMapper.toUserProfileResponse(user);
    }

    @Override
    public UserProfileResponse createUser(UserCreateRequest request) {
        User user = userMapper.toEntity(request);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setIsActive(true);
        User savedUser = userRepository.save(user);
        return userMapper.toUserProfileResponse(savedUser);
    }

    @Override
    public UserProfileResponse updateRole(Integer userId, UpdateRoleRequest updateRoleRequest) {
        User targetUser = userRepository.findById(userId.longValue())
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));
        if(targetUser.getRole() == Role.ADMIN) {
            throw new ForbiddenOperationException("Không thể thay đổi vai trò của ADMIN");
        }
        targetUser.setRole(updateRoleRequest.getRole());
        User updatedUser = userRepository.save(targetUser);
        return userMapper.toUserProfileResponse(updatedUser);
    }

    @Override
    public UserProfileResponse toggleUserStatus(Integer userId, UpdateStatusRequest request) {
        User targetUser = userRepository.findById(userId.longValue())
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));
        if (targetUser.getRole() == Role.ADMIN) {
            throw new ForbiddenOperationException("Không thể thay đổi trạng thái tài khoản ADMIN");
        }
        targetUser.setIsActive(request.getIsActive());
        User updatedUser = userRepository.save(targetUser);
        return userMapper.toUserProfileResponse(updatedUser);
    }

    @Override
    public void deleteUser(Integer userId) {
        User targetUser = userRepository.findById(userId.longValue())
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));
        if (targetUser.getRole() == Role.ADMIN) {
            throw new ForbiddenOperationException("Không thể xóa tài khoản ADMIN");
        }
        userRepository.delete(targetUser);
    }

    @Override
    public UserProfileResponse updateProfile(Integer userId, UserUpdateRequest request, User currentUser) {
        User targetUser = userRepository.findById(userId.longValue())
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));

        if (currentUser.getRole() != Role.ADMIN && !targetUser.getUserId().equals(currentUser.getUserId())) {
            throw new ForbiddenOperationException("Bạn không có quyền cập nhật thông tin người khác");
        }

        if (!targetUser.getEmail().equals(request.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new ResourceAlreadyExistsException("Email đã được sử dụng");
            }
        }

        targetUser.setFullName(request.getFullName());
        targetUser.setEmail(request.getEmail());

        User updatedUser = userRepository.save(targetUser);
        return userMapper.toUserProfileResponse(updatedUser);
    }

    @Override
    public void changePassword(Integer userId, ChangePasswordRequest request, User currentUser) {
        User targetUser = userRepository.findById(userId.longValue())
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));

        boolean isOwner = targetUser.getUserId().equals(currentUser.getUserId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new ForbiddenOperationException("Bạn không có quyền đổi mật khẩu của người khác");
        }

        // Nếu là chủ sở hữu tài khoản đang tự đổi, bắt buộc phải cung cấp mật khẩu cũ và kiểm tra
        if (isOwner) {
            if (request.getOldPassword() == null || request.getOldPassword().isEmpty()) {
                throw new BadRequestException("Vui lòng nhập mật khẩu cũ");
            }
            if (!passwordEncoder.matches(request.getOldPassword(), targetUser.getPasswordHash())) {
                throw new BadRequestException("Mật khẩu cũ không chính xác");
            }
        }

        // Nếu là ADMIN đổi mật khẩu cho người dùng khác, bỏ qua check mật khẩu cũ

        targetUser.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(targetUser);
    }
}
