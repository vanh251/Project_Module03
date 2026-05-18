package ra.edu.service;

import org.springframework.data.domain.Page;
import ra.edu.dto.request.NotificationRequest;
import ra.edu.dto.response.NotificationResponse;
import ra.edu.entity.User;

public interface NotificationService {
    Page<NotificationResponse> getMyNotifications(User currentUser, int page, int size);
    NotificationResponse markAsRead(Integer notificationId, User currentUser);
    NotificationResponse createNotification(NotificationRequest request);
    void deleteNotification(Integer notificationId);
}
