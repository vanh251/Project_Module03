package ra.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ra.edu.dto.response.NotificationResponse;
import ra.edu.entity.Notification;
import ra.edu.entity.User;
import ra.edu.mapper.NotificationMapper;
import ra.edu.repository.NotificationRepository;
import ra.edu.repository.UserRepository;
import ra.edu.service.NotificationService;
import ra.edu.config.exception.ResourceNotFoundException;
import ra.edu.config.exception.ForbiddenOperationException;
import ra.edu.dto.request.NotificationRequest;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getMyNotifications(User currentUser, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Notification> notifications = notificationRepository
                .findAllByUserUserIdOrderByCreatedAtDesc(currentUser.getUserId(), pageable);
        return notifications.map(notificationMapper::toResponse);
    }

    @Override
    @Transactional
    public NotificationResponse markAsRead(Integer notificationId, User currentUser) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông báo"));

        if (!notification.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new ForbiddenOperationException("Bạn không có quyền đọc thông báo này");
        }

        notification.setIsRead(true);
        Notification saved = notificationRepository.save(notification);
        return notificationMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public NotificationResponse createNotification(NotificationRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        Notification notification = Notification.builder()
                .user(user)
                .title(request.getTitle())
                .message(request.getMessage())
                .isRead(false)
                .build();

        Notification saved = notificationRepository.save(notification);
        return notificationMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteNotification(Integer notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông báo"));
        notificationRepository.delete(notification);
    }
}

