package ra.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.request.NotificationRequest;
import ra.edu.dto.response.ApiResponse;
import ra.edu.dto.response.NotificationResponse;
import ra.edu.dto.response.PageResponse;
import ra.edu.entity.User;
import ra.edu.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public ApiResponse<PageResponse<NotificationResponse>> getMyNotifications(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal User currentUser
    ) {
        Page<NotificationResponse> notificationPage = notificationService.getMyNotifications(currentUser, page, size);
        return ApiResponse.paginated(
                "Danh sách thông báo",
                notificationPage.getContent(),
                page,
                size,
                notificationPage.getTotalElements()
        );
    }

    @PutMapping("/{notification_id}/read")
    public ApiResponse<NotificationResponse> markAsRead(
            @PathVariable("notification_id") Integer notificationId,
            @AuthenticationPrincipal User currentUser
    ) {
        NotificationResponse response = notificationService.markAsRead(notificationId, currentUser);
        return ApiResponse.success("Đã đánh dấu thông báo là đã đọc", response);
    }

    @PostMapping
    public ApiResponse<NotificationResponse> createNotification(
            @Valid @RequestBody NotificationRequest request
    ) {
        NotificationResponse response = notificationService.createNotification(request);
        return ApiResponse.success("Tạo thông báo thành công", response);
    }

    @DeleteMapping("/{notification_id}")
    public ApiResponse<Void> deleteNotification(
            @PathVariable("notification_id") Integer notificationId
    ) {
        notificationService.deleteNotification(notificationId);
        return ApiResponse.success("Xóa thông báo thành công", null);
    }
}
