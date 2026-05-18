package ra.edu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ra.edu.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    Page<Notification> findAllByUserUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
